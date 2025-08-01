package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class ReleasedDrugs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String dispensedUuid = UUID.randomUUID().toString();
    private Long drugId;
    private Long patientId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "prescriptiondrug_id")
    private PrescribedDrugs prescribedDrug;

    private Long pharmacyId;


}
