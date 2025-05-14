package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescribedDrugResponse {

    private Long totalPage;
    private  PrescriptionsDto response;


}
