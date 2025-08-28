package com.medco.eprescription_out_of_stock.Dto.Request.Prescription;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetientDetail {

    private String patientFullName;
    private String cbhiId;
    private String gender;
    private Integer age;
    private String phoneNumber;
    private String houseNumber;
    private String idNumber;
    private String insuranceNumber;
    private String address;
    private String region;
    private String kebele;
    private String woreda;
    private String city;
    private double weight;
}
