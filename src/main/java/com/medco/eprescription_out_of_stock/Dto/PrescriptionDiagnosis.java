package com.medco.eprescription_out_of_stock.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionDiagnosis {
    @JsonProperty("diagnosisTypeId")
    private String diagnosisTypeId;

    @JsonProperty("additionalInfo")
    private String additionalInfo;
}