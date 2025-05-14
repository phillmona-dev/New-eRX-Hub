package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "returnedPrescreptions")
public class ReturnedPrescriptions {
    private static final long serialVersionUID = 4768448303484614360L;


    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String returnedPrescriptionUuid= UUID.randomUUID().toString();

    @Size(max = 40)
    private String returnedBy;

    @Size(max = 500)
    private String comment;

    private Date returnedDate;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "prescribedDrug_id")
    private PrescribedDrugs prescribedDrugs;

    private Long drugId;

}
