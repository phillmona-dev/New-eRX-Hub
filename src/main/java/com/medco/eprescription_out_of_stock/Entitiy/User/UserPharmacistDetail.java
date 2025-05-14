package com.medco.eprescription_out_of_stock.Entitiy.User;
import com.medco.eprescription_out_of_stock.shared.enums.ApprovalStatus;
import com.medco.eprescription_out_of_stock.shared.enums.PharmacyPersonnelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserPharmacistDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String pharmacyUuid;
    private String pharmacyName;
    private String educationLevel;
    private String graduatedFrom;
    private String graduationYear;
    private String licenceNo;
    private String licenceExpirationDate;
    private String qualificationLevel;
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private PharmacyPersonnelType pharmacyPersonnelType;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;


//    @OneToMany(mappedBy = "pharmacist", cascade = CascadeType.ALL)
//    private List<EvidenceDocument> evidences = new ArrayList<>();



}


