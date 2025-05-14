package com.medco.eprescription_out_of_stock.Dto.Response.Dispense;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDto {

    private Long id;
    private String prescriberUuid;
    private String pharmacyName;
    private String diagnosis;
    private String chiefCompliant;
    private boolean isDeleted;
    PrescriptionDrugDto drug;

}
