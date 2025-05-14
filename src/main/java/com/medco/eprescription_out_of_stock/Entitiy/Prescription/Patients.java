package com.medco.eprescription_out_of_stock.Entitiy.Prescription;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Patients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Size(max = 40)
    private String patientUuid = UUID.randomUUID().toString();
    private String firstName;
    private String lastName;
    private String grandFatherName;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;
    private String idNumber;
    private String kebelle;
    private String employerName;
    private String city;
    private String woreda;
    private String region;
    private Long insuranceId;

}
