package com.medco.eprescription_out_of_stock.Dto.Response.Patient;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientResponse {

    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String grandFatherName;
    private String phoneNumber;
    private String idNumber;
    private String kebelle;
    private String employerName;
    private String city;
    private String woreda;
    private String region;
    private Long insuranceId;

}
