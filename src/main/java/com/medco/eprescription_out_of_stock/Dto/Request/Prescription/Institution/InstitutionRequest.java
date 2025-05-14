package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InstitutionRequest {

    private String name;
    private String phoneNUmber;
    private String category;
    private String institutionId;
    private String tinNumber;
    private String zone;
    private String woreda;
    private String kebelle;
    private String contractStartDate;
    private String contractEndDate;


    private List<BranchRequest> branches;


}
