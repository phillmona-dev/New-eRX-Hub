package com.medco.eprescription_out_of_stock.Dto.Response.User.Prescription;


import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.MedicineList;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PetientDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrescriptionOutOfStockResponse {

    private String patientFullName;
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

    private List<MedicineList> medicineLists;


}
