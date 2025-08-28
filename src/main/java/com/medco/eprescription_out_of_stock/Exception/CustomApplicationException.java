package com.medco.eprescription_out_of_stock.Exception;

import org.springframework.http.HttpStatus;

public class CustomApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;
    private final String details;

    public CustomApplicationException(String message, HttpStatus status) {
        this(message, status, null, null);
    }

    public CustomApplicationException(String message, HttpStatus status, String errorCode) {
        this(message, status, errorCode, null);
    }

    public CustomApplicationException(String message, HttpStatus status, String errorCode, String details) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.details = details;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "CustomApplicationException{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", details='" + details + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
