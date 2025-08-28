package com.medco.eprescription_out_of_stock.Entitiy.Prescription;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class PrescriptionoutOfStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prescription_uuid", unique = true, nullable = false)
    private String prescriptionUuid = UUID.randomUUID().toString();

    private String patientFullName;
    private String cbhiId;
    private String gender;
    private Integer age;
    private String phoneNumber;
    private String houseNumber;
    private String idNumber;
    private String insuranceNumber;
    private String address;
    private String region;
    private String kebele;
    private String woreda;
    private String city;
    private double weight;

    private String cardNumber;
    private String sponsorName;
    private String woredaId;
    private String paymentTypeId;
    private String patientTypeId;
    private String prescriberName;
    private String prescriberQualification;
    private String prescriberRegistrationNumber;
    private LocalDateTime prescriptionDate;
    private String prescriptionNumber;
    private String institutionId;
    private String diagnosis;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;

    private double prescriptionTotalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patients patient;



}
