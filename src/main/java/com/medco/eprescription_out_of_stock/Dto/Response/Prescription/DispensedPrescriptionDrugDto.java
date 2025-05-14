package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispensedPrescriptionDrugDto {

    private Long id;
    private String prescriberUuid;
    private String pharmacyName;
    private String diagnosis;
    private String chiefCompliant;
    private boolean isDeleted;
    private Long patientId;
    private String title;
    private String patientFirstName;
    private String patientFatherName;
    private String patientGrandFatherName;
    private String patientPhoneNumber;
    private String patientDateOfBirth;

}
