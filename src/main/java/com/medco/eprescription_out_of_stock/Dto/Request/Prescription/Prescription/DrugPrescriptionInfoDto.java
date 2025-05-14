package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrugPrescriptionInfoDto {

    private Long drugId;
    private double drugDose;
    private double price;
    private String drugDoseUnit;
    private String drugFrequency;
    private String drugRoute;
    private int drugDuration;
    private int drugDurationUnit;
    private double totalQuantity;
    private String quantityUnit;
    private String drugInstruction;
    private String drugAdditionalInstruction;

}
