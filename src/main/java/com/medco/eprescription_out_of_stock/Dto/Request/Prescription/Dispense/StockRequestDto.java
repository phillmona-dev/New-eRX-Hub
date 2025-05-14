package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Dispense;
import com.medco.eprescription_out_of_stock.shared.enums.ReasonEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequestDto {

    private String inventoryUuid;
    private ReasonEnum deductReason;
    private double issuedAmount;



}
