package com.medco.eprescription_out_of_stock.Dto.Response.User;

import com.medco.eprescription_out_of_stock.shared.enums.PharmacyPersonnelType;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserResponse {

    private Long id;
    private String userUuid;
    private String email;
    private String title;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String Gender;
    private String mobilePhone;
    private UserStatus userStatus;
    private UserType userType;
    private String profilePicture;
    private String roleUuid;
    private String roleName;
    private Date dateOfBirth;
    private String pharmacyUuid;
    private String pharmacyName;
    private PharmacyPersonnelType pharmacyPersonnelType;

    private long totalPages;

}
