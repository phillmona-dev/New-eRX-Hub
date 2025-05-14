package com.medco.eprescription_out_of_stock.Dto.Request.Drug;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DrugFormularyDtoRequest {

    private String indications;
    private String cautions;
    private String contraindications;
    private String drugInteractions;
    private String sideEffects;
    private String doseAndAdministration;
    private String storage;
}
