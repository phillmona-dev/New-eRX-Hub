package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class InstitutionUpdateRequest {

    private Long id;
    private String name;
    private String phoneNUmber;
    private String category;
    private String institutionId;
    private String tinNumber;
    private String zone;
    private String woreda;
    private String kebelle;

    private List<BranchUpdateRequest> branches;


}
