package com.medco.eprescription_out_of_stock.Entitiy.User;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescriptions;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
public class UserPatientDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String MRN;

    private String MPN;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescriptions> prescriptions;


}
