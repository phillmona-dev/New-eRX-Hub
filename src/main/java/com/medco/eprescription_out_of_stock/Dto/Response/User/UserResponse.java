package com.medco.eprescription_out_of_stock.Dto.Response.User;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import com.medco.eprescription_out_of_stock.shared.Audit;
import com.medco.eprescription_out_of_stock.shared.enums.Status;

import java.io.Serial;

@Getter
@Setter
public class UserResponse extends Audit {
    @Serial
    private static final long serialVersionUID = -257037957967607541L;
    private String userUuid;
    private String email;
    private String title;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String Gender;

    private String mobilePhone;

    @Enumerated(EnumType.STRING)
    private Status userStatus;
//    private String userType;

    private boolean emailVerificationStatus;
    private boolean phoneVerificationStatus;
//    private boolean isDeleted;
    private String roleUuid;
    private String roleName;
    private long totalPages;


}
