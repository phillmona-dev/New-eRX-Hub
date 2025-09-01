package com.medco.eprescription_out_of_stock.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("cbhiId")
    private String cbhiId;

    @JsonProperty("age")
    private int age;

    @JsonProperty("ageType")
    private String ageType;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("houseNumber")
    private String houseNumber;

    @JsonProperty("cardNumber")
    private String cardNumber;

    @JsonProperty("insuranceNumber")
    private String insuranceNumber;

    @JsonProperty("kebele")
    private String kebele;

    @JsonProperty("rowGuid")
    private String rowGuid;

    @JsonProperty("sponsorName")
    private String sponsorName;

    @JsonProperty("woredaId")
    private String woredaId;

    @JsonProperty("paymentTypeId")
    private String paymentTypeId;

    @JsonProperty("patientTypeId")
    private String patientTypeId;
}