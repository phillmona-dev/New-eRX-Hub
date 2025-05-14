package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchUpdateRequest {

    private  Long id;
    private String name;
    private String phoneNumber;
    @Size(max = 40)
    private String zone;

    @Size(max = 40)
    private String woreda;

    @Size(max = 50)
    private String kebelle;


}


