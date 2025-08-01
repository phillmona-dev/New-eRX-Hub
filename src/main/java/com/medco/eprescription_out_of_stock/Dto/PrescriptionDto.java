package com.medco.eprescription_out_of_stock.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrescriptionDto {
    private String patientName;
    private String patientContact;
    private String drugName;
    private String dosage;
    private Integer quantity;
    private String prescribedBy;
    private LocalDateTime prescriptionDate;
    private boolean isOutOfStock;
    private String location;
}
