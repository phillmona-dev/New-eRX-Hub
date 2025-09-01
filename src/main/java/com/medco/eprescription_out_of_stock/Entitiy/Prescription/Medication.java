package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String unit;
    private int quantity;
    private String description;
    private double totalPrice;

    private String numberOfDuration;
    private String administrationId;
    private String frequencyTypeId;
    private String itemUnitId;
    private int orderNumber;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private PrescriptionoutOfStock prescription;

}
