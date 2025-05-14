package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Drug;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrugInfoDto {

    private String indications;
    private String cautions;
    private String drugInteractions;
    private String contraIndications;
    private String sideEffects;
    private String doseAndAdministration;
    private String storage;

}
