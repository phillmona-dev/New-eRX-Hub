package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Dispense;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DispenseRequestDto {

    private Long prescriptionId;
    private String Description;
    private List<DispensedDrugDto> prescribedDrugs;
}
