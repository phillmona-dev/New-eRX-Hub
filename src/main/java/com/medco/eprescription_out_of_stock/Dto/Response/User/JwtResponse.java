package com.medco.eprescription_out_of_stock.Dto.Response.User;
import lombok.Getter;
import lombok.Setter;
import com.medco.eprescription_out_of_stock.shared.enums.Status;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String userUuid;
    private String email;
    private String roleUuid;       // Add this
    private String roleName;
    private String title;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String mobilePhone;
    private Status userStatus;
    private String userType;
    private String providerUuid;
    private String payerUuid;
    private String profilePicture;
    private int branchId;          // Add this
    private List<String> privileges;

    // Updated constructor to include roleUuid and branchId
    public JwtResponse(
            String accessToken,
            String userUuid,
            String email,
            String roleUuid,
            String roleName,
            String title,
            String firstName,
            String fatherName,
            String grandFatherName,
            String gender,
            String mobilePhone,
            Status userStatus,
            String profilePicture,
            List<String> privileges
    ) {
        this.token = accessToken;
        this.userUuid = userUuid;
        this.email = email;
        this.roleUuid = roleUuid;
        this.roleName = roleName;
        this.title = title;
        this.firstName = firstName;
        this.fatherName = fatherName;
        this.grandFatherName = grandFatherName;
        this.gender = gender;
        this.mobilePhone = mobilePhone;
        this.userStatus = userStatus;
        this.userType = userType;
        this.providerUuid = providerUuid;
        this.payerUuid = payerUuid;
        this.profilePicture = profilePicture;
        this.privileges = privileges;
    }

    }

