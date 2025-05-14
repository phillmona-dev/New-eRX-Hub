package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;
import com.medco.eprescription_out_of_stock.shared.enums.DrugStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DrugResponseFromStock {

    private String drugUuid;
    private String drugName;
    private String drugCode;
    private String category;
    private String subCategory;
    private String strength;
    private String volume;
    private String drugUnit;
    private String drugDescription;
    private String dosageForm;
    private DrugStatusEnum drugStatus;
    private boolean isDeleted;
    private List<DrugBrandResponse> brand;


}
