package com.medco.eprescription_out_of_stock.Dto.Response.User;
import com.medco.eprescription_out_of_stock.shared.enums.PharmacyPersonnelType;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PharmacistResponse {

    // Fields for success scenarios
    private String email;
    private String title;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private Date dateOfBirth;
    private String mobilePhone;

    private UserStatus userStatus;
    private UserType userType;
    private String roleUuid;
    private String educationLevel;
    private String graduatedFrom;
    private String graduationYear;
    private String licenceNo;
    private String licenceExpirationDate;
    private String qualificationLevel;
    private Boolean isDeleted;
    private String pharmacyUuid;
    private String pharmacyName;
    private PharmacyPersonnelType pharmacyPersonnelType;

    private long totalPages;

    private Boolean success;
    private String message;

    public PharmacistResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

