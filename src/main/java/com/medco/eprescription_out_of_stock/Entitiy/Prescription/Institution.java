package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNUmber;
    private String institutionId;
    private String contractStartDate;
    private String contractEndDate;
    private String tinNumber;
    private String category;
    private String address;

    @Size(max = 40)
    private String zone;

    @Size(max = 40)
    private String woreda;

    @Size(max = 50)
    private String kebelle;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Branch> branches;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;
}
