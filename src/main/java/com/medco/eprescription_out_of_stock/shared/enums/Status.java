package com.medco.eprescription_out_of_stock.shared.enums;

public enum Status {
    ACTIVE,      // User is active and has full access
    INACTIVE,    // User is inactive and cannot log in
    SUSPENDED,   // User is temporarily suspended
    PENDING,     // User's registration or verification is pending
    DELETED,     // User account has been soft deleted
    BANNED
}
