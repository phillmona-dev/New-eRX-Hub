package com.medco.eprescription_out_of_stock.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionProcessingResponse {
    
    private boolean success;
    private String message;
    private String uniqueIdentifier;
    private String prescriptionUuid;
    private LocalDateTime processedAt;
    
    // Patient information
    private String patientName;
    private String patientPhone;
    
    // Processing details
    private ProcessingDetails processingDetails;
    
    // Medicine availability summary
    private List<MedicineAvailabilityInfo> medicineAvailability;
    
    // System status
    private SystemStatus systemStatus;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProcessingDetails {
        private boolean patientCreated;
        private boolean prescriptionSaved;
        private boolean stockChecked;
        private boolean messageSent;
        private int medicinesChecked;
        private int medicinesAvailable;
        private long processingTimeMs;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicineAvailabilityInfo {
        private String medicineName;
        private boolean available;
        private String availabilityStatus;
        private List<PharmacyLocation> locations;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PharmacyLocation {
        private String branchName;
        private String availabilityStatus;
        private String contactNumber;
        private String address;
        private Double distance;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SystemStatus {
        private boolean stockApiAvailable;
        private boolean smsServiceAvailable;
        private Map<String, String> externalSystemStatus;
        private List<String> warnings;
        private List<String> errors;
    }
    
    // Factory methods for common responses
    public static PrescriptionProcessingResponse success(String uniqueIdentifier, String prescriptionUuid) {
        return PrescriptionProcessingResponse.builder()
                .success(true)
                .message("Prescription processed successfully")
                .uniqueIdentifier(uniqueIdentifier)
                .prescriptionUuid(prescriptionUuid)
                .processedAt(LocalDateTime.now())
                .build();
    }
    
    public static PrescriptionProcessingResponse failure(String message) {
        return PrescriptionProcessingResponse.builder()
                .success(false)
                .message(message)
                .processedAt(LocalDateTime.now())
                .build();
    }
    
    public static PrescriptionProcessingResponse partialSuccess(String uniqueIdentifier, String prescriptionUuid, String message) {
        return PrescriptionProcessingResponse.builder()
                .success(true)
                .message(message)
                .uniqueIdentifier(uniqueIdentifier)
                .prescriptionUuid(prescriptionUuid)
                .processedAt(LocalDateTime.now())
                .build();
    }
}
