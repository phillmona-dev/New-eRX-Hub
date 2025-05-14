package com.medco.eprescription_out_of_stock.Dto.Request.User;
import com.medco.eprescription_out_of_stock.shared.enums.HealthCenterType;
import com.medco.eprescription_out_of_stock.shared.enums.OwnershipType;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthCenterSignUpRequest {

    private String email;
    private String password;
    private String name;
    private String address;
    private String mobilePhone;
    //    private String username;
    private HealthCenterType healthCenterType;
    private OwnershipType ownershipType;
    private UserType userType;
    private String roleUuid;
//    private UserStatus userStatus;
    private List<MultipartFile> files;
}
