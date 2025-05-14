package com.medco.eprescription_out_of_stock.Entitiy.User;

import com.medco.eprescription_out_of_stock.shared.Audit;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "mobilePhone") })
public class User extends Audit {

    @Serial
    private static final long serialVersionUID = 8259648969501526477L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 36, max = 40)
    private String userUuid = UUID.randomUUID().toString();

    @NotBlank
    @Size(min = 5, max = 50)
    @Email
    private String email;

    private String roleName;

    private Date dateOfBirth;

    @NotBlank
    @Size(min = 5, max = 120)
    private String password;

    @NotNull
    private String roleUuid;

    @Size(min = 2, max = 25)
    private String title;

    @Size(min = 2, max = 25)
    private String firstName;

    @Size(min = 2, max = 25)
    private String fatherName;

    @Size(min = 2, max = 25)
    private String grandFatherName;

    @Size(min = 1, max = 10)
    private String gender;

    @Size(min = 9, max = 13)
    private String mobilePhone;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String passwordResetCode;

    private String emailVerificationToken;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EvidenceDocument> evidences = new ArrayList<>();

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private UserPharmacistDetail pharmacistDetail;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();



}
