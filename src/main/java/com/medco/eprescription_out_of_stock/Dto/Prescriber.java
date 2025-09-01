package com.medco.eprescription_out_of_stock.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Prescriber {
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("qualification")
    private String qualification;

    @JsonProperty("registrationNumber")
    private String registrationNumber;

    @JsonProperty("rowGuid")
    private UUID rowGuid;
}