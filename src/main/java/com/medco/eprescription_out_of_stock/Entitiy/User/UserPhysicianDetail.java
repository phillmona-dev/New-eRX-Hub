package com.medco.eprescription_out_of_stock.Entitiy.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserPhysicianDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String educationLevel;
    private String graduatedFrom;
    private String graduationYear;
    private String licenceNo;
    private String licenceExpirationDate;
    private String qualificationLevel;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    private String institutionName;

}










