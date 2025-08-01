package com.medco.eprescription_out_of_stock.Dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class IncomingPrescriptionDto {
    private Prescriber prescriber;
    private Patient patient;
    private List<PrescriptionDetail> prescriptionDetails;
    private List<PrescriptionDiagnosis> prescriptionDiagnosis;
    private LocalDateTime prescriptionDate;
    private String prescriptionNumber;
    private String rowGuid;
    private String institutionId;
    private String prescriptionUUID;

    @Getter
    @Setter
    public static class Prescriber {
        private String firstName;
        private String middleName;
        private String lastName;
        private String qualification;
        private String registrationNumber;
        private UUID rowGuid;
    }

    @Getter
    @Setter
    public static class Patient {
        private String firstName;
        private String middleName;
        private String lastName;
        private String phoneNumber;
        private int age;
        private String ageType;
        private double weight;
        private String sex;
        private String houseNumber;
        private String cardNumber;
        private String insuranceNumber;
        private String kebele;
        private String rowGuid;
        private String sponsorName;
        private String woredaId;
        private String paymentTypeId;
        private String patientTypeId;
    }

    @Getter
    @Setter
    public static class PrescriptionDetail {
        private String medicationName;// added
        private int numberOfDuration;
        private String administrationId;
        private String frequencyTypeId;
        private String itemUnitId;
        private int quantity;
        private String additionalNote;
        private int orderNumber;
    }

    @Getter
    @Setter
    public static class PrescriptionDiagnosis {
        private String diagnosisTypeId;
        private String additionalInfo;
    }
}
