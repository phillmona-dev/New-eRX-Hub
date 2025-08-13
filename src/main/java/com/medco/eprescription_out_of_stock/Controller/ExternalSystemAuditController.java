package com.medco.eprescription_out_of_stock.Controller;

import com.medco.eprescription_out_of_stock.Entitiy.ExternalSystemAuditLog;
import com.medco.eprescription_out_of_stock.Service.ExternalSystemAuditService;
import com.medco.eprescription_out_of_stock.shared.enums.AuditLogStatus;
import com.medco.eprescription_out_of_stock.shared.enums.ExternalSystemType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/audit/external-systems")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExternalSystemAuditController {

    private final ExternalSystemAuditService auditService;

    /**
     * Search audit logs with various filters
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ExternalSystemAuditLog>> searchAuditLogs(
            @RequestParam(required = false) ExternalSystemType systemType,
            @RequestParam(required = false) AuditLogStatus status,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String prescriptionUuid,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "requestTimestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ExternalSystemAuditLog> logs = auditService.searchLogs(
                systemType, status, operation, prescriptionUuid, phoneNumber, pageable
        );
        
        return ResponseEntity.ok(logs);
    }

    /**
     * Get audit logs by prescription UUID
     */
    @GetMapping("/prescription/{prescriptionUuid}")
    public ResponseEntity<List<ExternalSystemAuditLog>> getLogsByPrescription(
            @PathVariable String prescriptionUuid
    ) {
        List<ExternalSystemAuditLog> logs = auditService.getLogsByPrescriptionUuid(prescriptionUuid);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get audit logs by patient phone number
     */
    @GetMapping("/patient/{phoneNumber}")
    public ResponseEntity<List<ExternalSystemAuditLog>> getLogsByPatient(
            @PathVariable String phoneNumber
    ) {
        List<ExternalSystemAuditLog> logs = auditService.getLogsByPatientPhone(phoneNumber);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get audit logs by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<ExternalSystemAuditLog>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestTimestamp"));
        Page<ExternalSystemAuditLog> logs = auditService.getLogsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get system statistics for a specific external system
     */
    @GetMapping("/statistics/{systemType}")
    public ResponseEntity<Map<String, Object>> getSystemStatistics(
            @PathVariable ExternalSystemType systemType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since
    ) {
        if (since == null) {
            since = LocalDateTime.now().minusDays(7); // Default to last 7 days
        }
        
        Map<String, Object> statistics = auditService.getSystemStatistics(systemType, since);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get overall system health metrics
     */
    @GetMapping("/health-metrics")
    public ResponseEntity<Map<String, Object>> getSystemHealthMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since
    ) {
        if (since == null) {
            since = LocalDateTime.now().minusDays(1); // Default to last 24 hours
        }
        
        Map<String, Object> healthMetrics = auditService.getSystemHealthMetrics(since);
        return ResponseEntity.ok(healthMetrics);
    }

    /**
     * Get failed requests for retry analysis
     */
    @GetMapping("/failed-requests")
    public ResponseEntity<List<ExternalSystemAuditLog>> getFailedRequests(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since
    ) {
        if (since == null) {
            since = LocalDateTime.now().minusHours(1); // Default to last hour
        }
        
        List<ExternalSystemAuditLog> failedRequests = auditService.getFailedRequestsForRetry(since);
        return ResponseEntity.ok(failedRequests);
    }

    /**
     * Get audit log by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExternalSystemAuditLog> getAuditLogById(@PathVariable Long id) {
        // This would require adding a method to the service
        return ResponseEntity.notFound().build(); // Placeholder
    }

    /**
     * Get system types enum values
     */
    @GetMapping("/system-types")
    public ResponseEntity<ExternalSystemType[]> getSystemTypes() {
        return ResponseEntity.ok(ExternalSystemType.values());
    }

    /**
     * Get audit log status enum values
     */
    @GetMapping("/statuses")
    public ResponseEntity<AuditLogStatus[]> getAuditLogStatuses() {
        return ResponseEntity.ok(AuditLogStatus.values());
    }

    /**
     * Get summary statistics for dashboard
     */
    @GetMapping("/dashboard-summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since
    ) {
        if (since == null) {
            since = LocalDateTime.now().minusDays(1);
        }
        
        Map<String, Object> summary = auditService.getSystemHealthMetrics(since);
        summary.put("timeRange", "Last 24 hours");
        summary.put("generatedAt", LocalDateTime.now());
        
        return ResponseEntity.ok(summary);
    }
}
