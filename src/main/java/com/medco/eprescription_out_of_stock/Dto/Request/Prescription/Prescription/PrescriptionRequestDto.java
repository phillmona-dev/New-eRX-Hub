package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrescriptionRequestDto {

    private String trackingNumber;
    private String pickedBy;
    private String diagnosis;
    private String chiefCompliant;
    private Long pharmacyId;
    private Long patientId;
    private List<DrugPrescriptionInfoDto> drugs;


}
