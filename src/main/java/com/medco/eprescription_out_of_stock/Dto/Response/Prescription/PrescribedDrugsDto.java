package com.medco.eprescription_out_of_stock.Dto.Response.Prescription;
import com.medco.eprescription_out_of_stock.shared.enums.prescribedDrugsEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescribedDrugsDto {

    private Long id;
    private String prescribedDrugsUuid;
    private double drugDose;
    private String drugDoseUnit;
    private String drugFrequency;
    private String drugRoute;
    private int drugDuration;
    private int drugDurationUnit;
    private double totalQuantity;
    private String quantityUnit;
    private String drugInstruction;
    private String drugAdditionalInstruction;
    private String trackingNumber;
    private String dispensedByUuid;
    private String pharmacyUuid;
    private String pickedBy;
    private Date despensedDate;
    private boolean isReleased;
    private Date releasedDate;
    private String releasedByUuid;
    private prescribedDrugsEnum prescribedDrugsStatus;


}

