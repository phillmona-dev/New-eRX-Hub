package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedPrescriptionDto {

    private Long id;
    private String returnedPrescriptionUuid;
    private String returnedByUuid;
    private String comment;
    private Date returnedDate;
    private boolean isDeleted;

}

