package com.medco.eprescription_out_of_stock.Service.inventory;

import com.medco.eprescription_out_of_stock.Dto.inventory.InventoryDto;
import com.medco.eprescription_out_of_stock.Entitiy.Inventory;

import java.util.List;

public interface InventoryService {
    Inventory addInventory(Long pharmacyId, InventoryDto inventoryDto);
    Inventory updateInventory(Long inventoryId, InventoryDto inventoryDto);
    void removeInventory(Long inventoryId);
    List<Inventory> getPharmacyInventory(Long pharmacyId);


    void checkAndNotifyAvailability(Long pharmacyId, Long drugId);

}
