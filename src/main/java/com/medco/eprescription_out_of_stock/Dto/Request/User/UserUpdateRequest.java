package com.medco.eprescription_out_of_stock.Dto.Request.User;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class UserUpdateRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

//    @NotBlank
//    @Size(min = 6, max = 40)
//    private String password;

    @NotBlank
    @Size(min = 2, max = 25)
    private String title;

    @NotBlank
    @Size(min = 2, max = 25)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 25)
    private String fatherName;

    @NotBlank
    @Size(min = 2, max = 25)
    private String grandFatherName;

    @NotBlank
    @Size(min = 1, max = 10)
    private String Gender;

    private Date dateOfBirth;

    @NotBlank
    @Size(min = 9, max = 13)
    private String mobilePhone;

//    private UserStatus userStatus;

    @NotNull
    private String roleUuid;

    private UserType userType;
}
