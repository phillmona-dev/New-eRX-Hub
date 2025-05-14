package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionsDto {
    private Long id;
    private String prescriberUuid;
    private String pharmacyName;
    private String diagnosis;
    private String chiefCompliant;
    private boolean isDeleted;
    List<PrescribedDrugsDto> prescribedDrug;

}
