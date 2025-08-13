package com.medco.eprescription_out_of_stock.Entitiy;

import com.medco.eprescription_out_of_stock.shared.enums.ExternalSystemType;
import com.medco.eprescription_out_of_stock.shared.enums.AuditLogStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "external_system_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalSystemAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExternalSystemType systemType;

    @Column(nullable = false)
    private String operation;

    @Column(nullable = false, length = 1000)
    private String requestUrl;

    @Column(columnDefinition = "TEXT")
    private String requestPayload;

    @Column(columnDefinition = "TEXT")
    private String responsePayload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditLogStatus status;

    private Integer responseCode;

    @Column(length = 2000)
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime requestTimestamp;

    private LocalDateTime responseTimestamp;

    private Long executionTimeMs;

    private String prescriptionUuid;
    private String patientPhoneNumber;
    private String medicationName;

    @Column(length = 500)
    private String userAgent;

    @Column(length = 100)
    private String correlationId;

    @Column(columnDefinition = "TEXT")
    private String additionalMetadata;

    @PrePersist
    protected void onCreate() {
        if (requestTimestamp == null) {
            requestTimestamp = LocalDateTime.now();
        }
    }

    public void markAsCompleted(String responsePayload, Integer responseCode) {
        this.responsePayload = responsePayload;
        this.responseCode = responseCode;
        this.responseTimestamp = LocalDateTime.now();
        this.executionTimeMs = java.time.Duration.between(requestTimestamp, responseTimestamp).toMillis();
        this.status = responseCode >= 200 && responseCode < 300 ? AuditLogStatus.SUCCESS : AuditLogStatus.FAILED;
    }

    public void markAsFailed(String errorMessage, Integer responseCode) {
        this.errorMessage = errorMessage;
        this.responseCode = responseCode;
        this.responseTimestamp = LocalDateTime.now();
        this.executionTimeMs = java.time.Duration.between(requestTimestamp, responseTimestamp).toMillis();
        this.status = AuditLogStatus.FAILED;
    }

    public void markAsTimeout() {
        this.errorMessage = "Request timed out";
        this.responseTimestamp = LocalDateTime.now();
        this.executionTimeMs = java.time.Duration.between(requestTimestamp, responseTimestamp).toMillis();
        this.status = AuditLogStatus.TIMEOUT;
    }
}
