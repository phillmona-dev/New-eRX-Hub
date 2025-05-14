package com.medco.eprescription_out_of_stock.Dto.Request.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PhysicianSignupRequest extends UserRequest {
//    private String firstName;
//    private String lastName;
//    private String grandFatherName;
//    private String gender;
//    private String title;
//    private String email;
//    private String mobilePhone;
//    private String password;
    private String graduatedFrom;
    private String graduationYear;
    private String educationLevel;
    private String qualificationLevel;
    private String institutionName;
    private String licenceNo;
//    private String roleUuid;
    private String licenceExpirationDate;
    private List<MultipartFile> files;
}
