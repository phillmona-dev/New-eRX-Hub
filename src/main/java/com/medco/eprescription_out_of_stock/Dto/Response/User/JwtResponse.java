package com.medco.eprescription_out_of_stock.Dto.Response.User;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
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
//    private String roleName;
    private String title;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String mobilePhone;
    private UserStatus userStatus;
    private UserType userType;
//    private String insuranceUuid;
//    private String agencyUuid;
//    private String profilePicture;
//    private int branchId;
    private List<String> privileges;

   public JwtResponse(String accessToken, String userUuid, String email, String roleUuid, String title,
                       String firstName, String fatherName, String grandFatherName, String gender, String mobilePhone,
                       UserStatus userStatus, UserType userType,
                        List<String> privileges) {
        this.token = accessToken;
        this.userUuid = userUuid;
        this.email = email;
        this.roleUuid = roleUuid;
//        this.roleName = roleName;
        this.title = title;
        this.firstName = firstName;
        this.fatherName = fatherName;
        this.grandFatherName = grandFatherName;
        this.gender = gender;
        this.mobilePhone = mobilePhone;
        this.userStatus = userStatus;
        this.userType = userType;
//        this.insuranceUuid = insuranceUuid;
//        this.agencyUuid = agencyUuid;
//      this.profilePicture = profilePicture;
//        this.branchId = branchId;
        this.privileges = privileges;
    }
}
