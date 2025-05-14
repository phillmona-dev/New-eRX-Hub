package com.medco.eprescription_out_of_stock.Dto.Response.Dispense;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispensedDrugsResponseDto {

    private Long id;
    private String drugDispenseUuid;
    private String dispensedBy;
    private String dispensedDate;
    private Long patientId;
    private Long patientName;

    PrescriptionDto prescription;


}
