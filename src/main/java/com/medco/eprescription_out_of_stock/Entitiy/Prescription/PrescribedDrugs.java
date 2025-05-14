package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.shared.enums.prescribedDrugsEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescribedDrugs")
public class PrescribedDrugs implements Serializable {

    @Serial
    private static final long serialVersionUID = 4768448303484614360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String prescribedDrugsUuid = UUID.randomUUID().toString();

    private double drugDose;
    @Size(max = 30)
    private String drugDoseUnit;

    @Size(max = 50)
    private String drugFrequency;

    @Size(max = 30)
    private String drugRoute;

    private int drugDuration;

    private int drugDurationUnit;

    private double totalQuantity;
    private double price;

    @Size(max = 100)
    private String quantityUnit;

    @Size(max = 100)
    private String drugInstruction;

    @Size(max = 500)
    private String drugAdditionalInstruction;

    @Size(max = 50)
    private String trackingNumber;

    private String dispensedBy;

    @Size(max = 60)
    private String pickedBy;

    private Date despensedDate;

    private boolean isReleased;

    private Date releasedDate;

    private String releasedBy;

    @Enumerated(EnumType.STRING)
    private prescribedDrugsEnum prescribedDrugsStatus=prescribedDrugsEnum.prescribed;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "prescription_id")
    private Prescriptions prescriptions;

    @OneToMany(mappedBy = "prescribedDrugs")
    private List<ReturnedPrescriptions> returnedPrescriptions;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

}
