package com.medco.eprescription_out_of_stock.Dto.Request.Drug;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DrugDtoRequest {

    private String drugName;
    private String drugCode;
    private String category;
    private String subCategory;
    private String strength;
    private String volume;
    private String drugUnit;
    private String dosageForm;
    private String drugDescription;

    private DrugFormularyDtoRequest drugFormularyDtoRequest;


}
