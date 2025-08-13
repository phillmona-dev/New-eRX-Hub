package com.medco.eprescription_out_of_stock.shared.enums;

public enum AuditLogStatus {
    PENDING("Request initiated, waiting for response"),
    SUCCESS("Request completed successfully"),
    FAILED("Request failed with error"),
    TIMEOUT("Request timed out"),
    CANCELLED("Request was cancelled"),
    RETRY("Request is being retried");

    private final String description;

    AuditLogStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
