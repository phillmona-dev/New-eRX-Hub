package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReturnedPrescriptionRequest {


    private String reason;
    private Date returnedDate;
    private Long prescriptionDrugUuid;
    private Long drugUuid;
    private Long prescriptionId;

}
