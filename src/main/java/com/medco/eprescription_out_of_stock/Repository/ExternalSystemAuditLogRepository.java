package com.medco.eprescription_out_of_stock.Repository;

import com.medco.eprescription_out_of_stock.Entitiy.ExternalSystemAuditLog;
import com.medco.eprescription_out_of_stock.shared.enums.AuditLogStatus;
import com.medco.eprescription_out_of_stock.shared.enums.ExternalSystemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalSystemAuditLogRepository extends JpaRepository<ExternalSystemAuditLog, Long> {

    // Find by system type
    Page<ExternalSystemAuditLog> findBySystemType(ExternalSystemType systemType, Pageable pageable);

    // Find by status
    Page<ExternalSystemAuditLog> findByStatus(AuditLogStatus status, Pageable pageable);

    // Find by prescription UUID
    List<ExternalSystemAuditLog> findByPrescriptionUuidOrderByRequestTimestampDesc(String prescriptionUuid);

    // Find by patient phone number
    List<ExternalSystemAuditLog> findByPatientPhoneNumberOrderByRequestTimestampDesc(String phoneNumber);

    // Find by correlation ID
    Optional<ExternalSystemAuditLog> findByCorrelationId(String correlationId);

    // Find failed requests for retry
    @Query("SELECT a FROM ExternalSystemAuditLog a WHERE a.status = :status AND a.requestTimestamp >= :since")
    List<ExternalSystemAuditLog> findFailedRequestsSince(@Param("status") AuditLogStatus status, @Param("since") LocalDateTime since);

    // Find by date range
    @Query("SELECT a FROM ExternalSystemAuditLog a WHERE a.requestTimestamp BETWEEN :startDate AND :endDate ORDER BY a.requestTimestamp DESC")
    Page<ExternalSystemAuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    // Statistics queries
    @Query("SELECT COUNT(a) FROM ExternalSystemAuditLog a WHERE a.systemType = :systemType AND a.status = :status AND a.requestTimestamp >= :since")
    Long countBySystemTypeAndStatusSince(@Param("systemType") ExternalSystemType systemType, @Param("status") AuditLogStatus status, @Param("since") LocalDateTime since);

    @Query("SELECT AVG(a.executionTimeMs) FROM ExternalSystemAuditLog a WHERE a.systemType = :systemType AND a.status = 'SUCCESS' AND a.requestTimestamp >= :since")
    Double getAverageExecutionTime(@Param("systemType") ExternalSystemType systemType, @Param("since") LocalDateTime since);

    // Search functionality
    @Query("SELECT a FROM ExternalSystemAuditLog a WHERE " +
           "(:systemType IS NULL OR a.systemType = :systemType) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:operation IS NULL OR LOWER(a.operation) LIKE LOWER(CONCAT('%', :operation, '%'))) AND " +
           "(:prescriptionUuid IS NULL OR a.prescriptionUuid = :prescriptionUuid) AND " +
           "(:phoneNumber IS NULL OR a.patientPhoneNumber = :phoneNumber) " +
           "ORDER BY a.requestTimestamp DESC")
    Page<ExternalSystemAuditLog> searchLogs(
            @Param("systemType") ExternalSystemType systemType,
            @Param("status") AuditLogStatus status,
            @Param("operation") String operation,
            @Param("prescriptionUuid") String prescriptionUuid,
            @Param("phoneNumber") String phoneNumber,
            Pageable pageable
    );
}
