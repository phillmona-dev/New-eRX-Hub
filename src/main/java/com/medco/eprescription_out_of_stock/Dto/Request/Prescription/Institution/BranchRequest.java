package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BranchRequest {

    private String name;
    private String phoneNumber;
    private String zone;
    private String woreda;
    private String kebelle;

}
