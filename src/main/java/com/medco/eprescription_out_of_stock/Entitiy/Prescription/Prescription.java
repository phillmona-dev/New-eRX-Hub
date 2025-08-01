package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import com.medco.eprescription_out_of_stock.Entitiy.User.UserPatientDetail;
import com.medco.eprescription_out_of_stock.shared.enums.PrescriptionStatus;
import com.medco.eprescription_out_of_stock.shared.enums.prescriptionsStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prescriptionUuid = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String patientContact;

    @Column(nullable = false)
    private String drugName;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String prescribedBy;

    @Column(nullable = false)
    private LocalDateTime prescriptionDate;

    private String chiefCompliant;
    private String diagnosis;

    @Column(nullable = false)
    private boolean isOutOfStock;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status;

    private boolean isDeleted = false;

    @Size(max=255)
    private  String physicianName;

    @Size(max=255)
    private  String pharmacistName;

    @Enumerated(EnumType.STRING)
    private prescriptionsStatusEnum prescriptionsStatus=prescriptionsStatusEnum.pending;

    @ManyToOne
    @JoinColumn(name = "pharmacyId", referencedColumnName = "id", nullable = false)
    private Pharmacy pharmacy;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private UserPatientDetail patient;


    @OneToMany(mappedBy = "prescriptions", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescribedDrugs> prescribedDrugs;

}
