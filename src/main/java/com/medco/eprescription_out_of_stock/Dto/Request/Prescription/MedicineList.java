package com.medco.eprescription_out_of_stock.Dto.Request.Prescription;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MedicineList {

    private String name;
    private String unit;
    private int quantity;
    private String description;
    private double totalPrice;

    private int numberOfDuration;
    private String administrationId;
    private String frequencyTypeId;
    private String itemUnitId;
    private int orderNumber;
}
