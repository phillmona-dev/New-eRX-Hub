
package com.medco.eprescription_out_of_stock.Dto.Request.User;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientSignUpRequestDto {
    @NotBlank
    @Size(min = 5, max = 50)

    private String email;
    @Size(min = 5, max = 120)
    private String password;

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
    private String gender;

    private String dateOfBirth;

    @NotBlank
    @Size(min = 9, max = 13)
    private String mobilePhone;

//    private UserType userType;

    @Enumerated(EnumType.STRING)
    private Status userStatus;

    @NotNull
    private String roleUuid;



}
