package com.medco.eprescription_out_of_stock.shared.enums;

public enum ExternalSystemType {
    KENEMA_STOCK_API("Kenema Stock Management System"),
    AFROMESSAGE_SMS("AfroMessage SMS Service"),
    INSURANCE_PROVIDER("Insurance Provider API"),
    PHARMACY_NETWORK("Pharmacy Network API"),
    EMR_SYSTEM("Electronic Medical Record System"),
    PAYMENT_GATEWAY("Payment Gateway"),
    NOTIFICATION_SERVICE("Notification Service");

    private final String description;

    ExternalSystemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
