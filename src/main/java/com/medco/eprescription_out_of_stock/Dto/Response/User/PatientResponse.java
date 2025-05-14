package com.medco.eprescription_out_of_stock.Dto.Response.User;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class PatientResponse {

    private String firstName;
    private String lastName;
    private String grandFatherName;
    private String mobilePhone;
    private String email;
    private Date dateOfBirth;
    private String MRN;
    private String MPN;
    private long totalPages;

}
