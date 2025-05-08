package com.medco.eprescription_out_of_stock.Security.ui;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String userUuid;
    private String email;
    private String roleUuid;
    private String roleName;
    private String title;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String mobilePhone;
    private Status userStatus;
    private String profilePicture;
    private List<String> privileges;

    // Constructor to initialize fields
    public JwtResponse(String accessToken, String userUuid, String email, String roleUuid, String roleName, String title, String firstName,
                       String fatherName, String grandFatherName, String gender, String mobilePhone, Status userStatus, String profilePicture, long sub_city_id,Long woreda_id , List<String> privileges) {
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
        this.profilePicture = profilePicture;
        this.privileges = privileges;
    }
}


