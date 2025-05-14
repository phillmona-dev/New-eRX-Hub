package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.pharmacy;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PharmacyRequestDto {
    private String pharmacyName;

    private String pharmacyType;

    private Integer countryId;

    private Integer stateId;

    private Integer zoneId;

    private Integer woredaId;

    private String kebelle;

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

    private String openingAt;
    private String closingAt;



}
