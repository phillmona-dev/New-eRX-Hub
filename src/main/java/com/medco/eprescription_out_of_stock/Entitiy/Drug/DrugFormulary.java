package com.medco.eprescription_out_of_stock.Entitiy.Drug;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
public class DrugFormulary implements Serializable {


    @Serial
    private static final long serialVersionUID = 4768448303484614360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String drugFormularyUuid = UUID.randomUUID().toString();
    private String indications;
    private String cautions;
    private String contraindications;
    private String drugInteractions;
    private String sideEffects;
    private String doseAndAdministration;
    private String storage;

    @OneToOne(mappedBy = "drugFormulary")
    @JsonBackReference
    private Drug drug;


}
