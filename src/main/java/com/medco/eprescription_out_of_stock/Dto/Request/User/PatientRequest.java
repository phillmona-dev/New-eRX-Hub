package com.medco.eprescription_out_of_stock.Dto.Request.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest extends UserRequest{

//    private Date dateOfBirth;
    private String MRN;
    private String MPN;

}
