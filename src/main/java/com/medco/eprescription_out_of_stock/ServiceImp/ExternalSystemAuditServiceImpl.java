package com.medco.eprescription_out_of_stock.ServiceImp;

import com.medco.eprescription_out_of_stock.Entitiy.ExternalSystemAuditLog;
import com.medco.eprescription_out_of_stock.Repository.ExternalSystemAuditLogRepository;
import com.medco.eprescription_out_of_stock.Service.ExternalSystemAuditService;
import com.medco.eprescription_out_of_stock.shared.enums.AuditLogStatus;
import com.medco.eprescription_out_of_stock.shared.enums.ExternalSystemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalSystemAuditServiceImpl implements ExternalSystemAuditService {

    private final ExternalSystemAuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public ExternalSystemAuditLog createAuditLog(ExternalSystemType systemType, String operation, String requestUrl,
                                                 String requestPayload, String prescriptionUuid, String patientPhoneNumber,
                                                 String medicationName, String correlationId) {
        
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        ExternalSystemAuditLog auditLog = ExternalSystemAuditLog.builder()
                .systemType(systemType)
                .operation(operation)
                .requestUrl(requestUrl)
                .requestPayload(requestPayload)
                .status(AuditLogStatus.PENDING)
                .prescriptionUuid(prescriptionUuid)
                .patientPhoneNumber(patientPhoneNumber)
                .medicationName(medicationName)
                .correlationId(correlationId)
                .requestTimestamp(LocalDateTime.now())
                .build();

        ExternalSystemAuditLog saved = auditLogRepository.save(auditLog);
        log.debug("Created audit log with ID: {} for system: {} operation: {}", saved.getId(), systemType, operation);
        return saved;
    }

    @Override
    @Transactional
    public void markAsSuccess(Long auditLogId, String responsePayload, Integer responseCode) {
        auditLogRepository.findById(auditLogId).ifPresent(auditLog -> {
            auditLog.markAsCompleted(responsePayload, responseCode);
            auditLogRepository.save(auditLog);
            log.debug("Marked audit log {} as successful with response code: {}", auditLogId, responseCode);
        });
    }

    @Override
    @Transactional
    public void markAsFailed(Long auditLogId, String errorMessage, Integer responseCode) {
        auditLogRepository.findById(auditLogId).ifPresent(auditLog -> {
            auditLog.markAsFailed(errorMessage, responseCode);
            auditLogRepository.save(auditLog);
            log.warn("Marked audit log {} as failed: {}", auditLogId, errorMessage);
        });
    }

    @Override
    @Transactional
    public void markAsTimeout(Long auditLogId) {
        auditLogRepository.findById(auditLogId).ifPresent(auditLog -> {
            auditLog.markAsTimeout();
            auditLogRepository.save(auditLog);
            log.warn("Marked audit log {} as timeout", auditLogId);
        });
    }

    @Override
    public List<ExternalSystemAuditLog> getLogsByPrescriptionUuid(String prescriptionUuid) {
        return auditLogRepository.findByPrescriptionUuidOrderByRequestTimestampDesc(prescriptionUuid);
    }

    @Override
    public List<ExternalSystemAuditLog> getLogsByPatientPhone(String phoneNumber) {
        return auditLogRepository.findByPatientPhoneNumberOrderByRequestTimestampDesc(phoneNumber);
    }

    @Override
    public Page<ExternalSystemAuditLog> searchLogs(ExternalSystemType systemType, AuditLogStatus status,
                                                  String operation, String prescriptionUuid, String phoneNumber,
                                                  Pageable pageable) {
        return auditLogRepository.searchLogs(systemType, status, operation, prescriptionUuid, phoneNumber, pageable);
    }

    @Override
    public Page<ExternalSystemAuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByDateRange(startDate, endDate, pageable);
    }

    @Override
    public List<ExternalSystemAuditLog> getFailedRequestsForRetry(LocalDateTime since) {
        return auditLogRepository.findFailedRequestsSince(AuditLogStatus.FAILED, since);
    }

    @Override
    public Map<String, Object> getSystemStatistics(ExternalSystemType systemType, LocalDateTime since) {
        Map<String, Object> stats = new HashMap<>();
        
        Long successCount = auditLogRepository.countBySystemTypeAndStatusSince(systemType, AuditLogStatus.SUCCESS, since);
        Long failedCount = auditLogRepository.countBySystemTypeAndStatusSince(systemType, AuditLogStatus.FAILED, since);
        Long timeoutCount = auditLogRepository.countBySystemTypeAndStatusSince(systemType, AuditLogStatus.TIMEOUT, since);
        Double avgExecutionTime = auditLogRepository.getAverageExecutionTime(systemType, since);
        
        Long totalRequests = successCount + failedCount + timeoutCount;
        Double successRate = totalRequests > 0 ? (successCount.doubleValue() / totalRequests) * 100 : 0.0;
        
        stats.put("systemType", systemType);
        stats.put("totalRequests", totalRequests);
        stats.put("successCount", successCount);
        stats.put("failedCount", failedCount);
        stats.put("timeoutCount", timeoutCount);
        stats.put("successRate", successRate);
        stats.put("averageExecutionTimeMs", avgExecutionTime != null ? avgExecutionTime : 0.0);
        stats.put("since", since);
        
        return stats;
    }

    @Override
    public Map<String, Object> getSystemHealthMetrics(LocalDateTime since) {
        Map<String, Object> healthMetrics = new HashMap<>();
        
        for (ExternalSystemType systemType : ExternalSystemType.values()) {
            Map<String, Object> systemStats = getSystemStatistics(systemType, since);
            healthMetrics.put(systemType.name().toLowerCase(), systemStats);
        }
        
        return healthMetrics;
    }

    @Override
    @Transactional
    public void cleanupOldLogs(LocalDateTime olderThan) {
        // TODO implemente with a custom query to delete old records

        log.info("Cleanup operation requested for logs older than: {}", olderThan);
    }
}
