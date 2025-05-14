package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;
import com.medco.eprescription_out_of_stock.shared.enums.DrugBrandsStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrugBrandResponse {
    private String drugBrandCode;
    private String drugBrandName;
    private DrugBrandsStatusEnum drugBrandsStatus;
    private boolean isDeleted;
    private String drugBrandUuid;
    private DrugInfoResponse drugInfo;


}
