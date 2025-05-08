package com.medco.eprescription_out_of_stock.Entitiy.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.medco.eprescription_out_of_stock.shared.Audit;
import com.medco.eprescription_out_of_stock.shared.enums.Status;

import java.util.UUID;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "mobilePhone")
})
public class User extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleUuid;

    @Size(min = 36, max = 40)
    private String userUuid = UUID.randomUUID().toString();

    @NotBlank
    @Size(min = 5, max = 50)
    private String email;

    @Size(min = 5, max = 120)
    private String password;


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
    private String wereda;
    private String subCity;
    private String region;
    private String city;
    @Enumerated(EnumType.STRING)
    private Status userStatus=Status.ACTIVE;
    @NotBlank
    @Size(min = 9, max = 13)
    @Column(nullable = false, unique = true)
    private String mobilePhone;
    private String passwordResetCode;
    private String emailVerificationToken;
    private String profilePicture;
    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;
    private String roleName;


}



