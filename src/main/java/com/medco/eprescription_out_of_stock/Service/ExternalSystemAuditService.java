package com.medco.eprescription_out_of_stock.Service;

import com.medco.eprescription_out_of_stock.Entitiy.ExternalSystemAuditLog;
import com.medco.eprescription_out_of_stock.shared.enums.AuditLogStatus;
import com.medco.eprescription_out_of_stock.shared.enums.ExternalSystemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ExternalSystemAuditService {

    /**
     * Create a new audit log entry for an external system call
     */
    ExternalSystemAuditLog createAuditLog(ExternalSystemType systemType, String operation, String requestUrl, 
                                         String requestPayload, String prescriptionUuid, String patientPhoneNumber, 
                                         String medicationName, String correlationId);

    /**
     * Update audit log with successful response
     */
    void markAsSuccess(Long auditLogId, String responsePayload, Integer responseCode);

    /**
     * Update audit log with failure information
     */
    void markAsFailed(Long auditLogId, String errorMessage, Integer responseCode);

    /**
     * Update audit log with timeout information
     */
    void markAsTimeout(Long auditLogId);

    /**
     * Get audit logs by prescription UUID
     */
    List<ExternalSystemAuditLog> getLogsByPrescriptionUuid(String prescriptionUuid);

    /**
     * Get audit logs by patient phone number
     */
    List<ExternalSystemAuditLog> getLogsByPatientPhone(String phoneNumber);

    /**
     * Search audit logs with filters
     */
    Page<ExternalSystemAuditLog> searchLogs(ExternalSystemType systemType, AuditLogStatus status, 
                                           String operation, String prescriptionUuid, String phoneNumber, 
                                           Pageable pageable);

    /**
     * Get audit logs by date range
     */
    Page<ExternalSystemAuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Get failed requests for retry
     */
    List<ExternalSystemAuditLog> getFailedRequestsForRetry(LocalDateTime since);

    /**
     * Get system statistics
     */
    Map<String, Object> getSystemStatistics(ExternalSystemType systemType, LocalDateTime since);

    /**
     * Get overall system health metrics
     */
    Map<String, Object> getSystemHealthMetrics(LocalDateTime since);

    /**
     * Clean up old audit logs (for maintenance)
     */
    void cleanupOldLogs(LocalDateTime olderThan);
}
