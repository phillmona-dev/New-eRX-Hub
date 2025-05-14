package com.medco.eprescription_out_of_stock.Dto.Response.Dispense;
import com.medco.eprescription_out_of_stock.shared.enums.prescribedDrugsEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PrescriptionDrugDto {
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
