package com.medco.eprescription_out_of_stock.Dto.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDto {
    private Long drugId;
    private Integer quantity;
    private Double price;
}
