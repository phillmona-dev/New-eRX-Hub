package com.medco.eprescription_out_of_stock.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IncomingPrescriptionDto {
    @JsonProperty("prescriber")
    private Prescriber prescriber;

    @JsonProperty("patient")
    private Patient patient;

    @JsonProperty("prescriptionDetails")
    private List<PrescriptionDetail> prescriptionDetails;

    @JsonProperty("prescriptionDiagnosis")
    private List<PrescriptionDiagnosis> prescriptionDiagnosis;

    @JsonProperty("prescriptionDate")
    private LocalDateTime prescriptionDate;

    @JsonProperty("prescriptionNumber")
    private String prescriptionNumber;

    @JsonProperty("rowGuid")
    private String rowGuid;

    @JsonProperty("institutionId")
    private String institutionId;

    @JsonProperty("prescriptionUUID")
    private String prescriptionUUID;
}
