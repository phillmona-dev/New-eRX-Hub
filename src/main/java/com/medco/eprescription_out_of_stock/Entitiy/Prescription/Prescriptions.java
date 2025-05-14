package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import com.medco.eprescription_out_of_stock.Entitiy.User.UserPatientDetail;
import com.medco.eprescription_out_of_stock.shared.enums.prescriptionsStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescriptions")
public class Prescriptions implements Serializable {

    @Serial
    private static final long serialVersionUID = 4768448303484614360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String prescriptionUuid = UUID.randomUUID().toString();

    @Size(max=255)
    private  String physicianName;


    @Size(max=255)
    private  String pharmacistName;

    private LocalDate prescriptionDate;

    @Size(max = 500)
    private String diagnosis;

    @Size(max = 500)
    private String chiefCompliant;

    @Enumerated(EnumType.STRING)
    private prescriptionsStatusEnum prescriptionsStatus=prescriptionsStatusEnum.pending;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "pharmacyId", referencedColumnName = "id", nullable = false)
    private Pharmacy pharmacy;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private UserPatientDetail patient;


    @OneToMany(mappedBy = "prescriptions", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescribedDrugs> prescribedDrugs;

}
