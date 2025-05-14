package com.medco.eprescription_out_of_stock.Dto.Response.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhysicianResponse {

    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String mobilePhone;
    private String email;
    private String educationLevel;
    private String graduatedFrom;
    private String graduationYear;
    private String licenceNo;
    private String licenceExpirationDate;
    private String qualificationLevel;
    private Boolean isDeleted;
    private long totalPages;
}
