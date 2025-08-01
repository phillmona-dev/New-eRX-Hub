package com.medco.eprescription_out_of_stock.Entitiy.Prescription;

import com.medco.eprescription_out_of_stock.shared.enums.PharmacyApproval;
import com.medco.eprescription_out_of_stock.shared.enums.PharmacyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Pharmacy {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private String pharmacyUuid = UUID.randomUUID().toString();

    private String pharmacyName;

    private String pharmacyType;

    private Integer countryId;

    private Integer stateId;

    private Integer zoneId;

    private Integer woredaId;

    private String streetNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private Double latitude;

    private Double longitude;

    private String licenceNumber;

    private String registrationNumber;

    private String tinNumber;


    @Column(name = "addedDate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addedDate;

    private Integer registeredById;

    @Column(nullable = false)
    private Integer isDeleted = 0;

    @Enumerated(EnumType.STRING)
    private PharmacyStatus status = PharmacyStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PharmacyApproval approval = PharmacyApproval.PENDING;

    private Integer approvedById;

    @Column(name = "approvedDate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime approvedDate;


    private String openingAt;
    private String closingAt;

}
