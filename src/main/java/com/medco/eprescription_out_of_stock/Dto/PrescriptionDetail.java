package com.medco.eprescription_out_of_stock.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionDetail {
    @JsonProperty("medicationName")
    private String medicationName;

    @JsonProperty("numberOfDuration")
    private String numberOfDuration;

    @JsonProperty("administrationId")
    private String administrationId;

    @JsonProperty("frequencyTypeId")
    private String frequencyTypeId;

    @JsonProperty("itemUnitId")
    private String itemUnitId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("additionalNote")
    private String additionalNote;

    @JsonProperty("orderNumber")
    private int orderNumber;
}