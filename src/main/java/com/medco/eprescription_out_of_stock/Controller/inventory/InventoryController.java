package com.medco.eprescription_out_of_stock.Controller.inventory;

import com.medco.eprescription_out_of_stock.Dto.inventory.InventoryDto;
import com.medco.eprescription_out_of_stock.Entitiy.Inventory;
import com.medco.eprescription_out_of_stock.Service.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/erx/pharmacies/{pharmacyId}/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> addInventory(@PathVariable Long pharmacyId, @RequestBody InventoryDto inventoryDto) {
        Inventory addedInventory = inventoryService.addInventory(pharmacyId, inventoryDto);
        return new ResponseEntity<>(addedInventory, HttpStatus.CREATED);
    }

//    @PutMapping("/{inventoryId}")
//    public ResponseEntity<Inventory> updateInventory(@PathVariable Long inventoryId, @RequestBody InventoryDto inventoryDto) {
//        Inventory updatedInventory = inventoryService.updateInventory(inventoryId, inventoryDto);
//        return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
//    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> removeInventory(@PathVariable Long inventoryId) {
        inventoryService.removeInventory(inventoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getPharmacyInventory(@PathVariable Long pharmacyId) {
        List<Inventory> inventory = inventoryService.getPharmacyInventory(pharmacyId);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long pharmacyId, @PathVariable Long inventoryId, @RequestBody InventoryDto inventoryDto) {
        Inventory updatedInventory = inventoryService.updateInventory(inventoryId, inventoryDto);
        return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
    }

    @PostMapping("/{drugId}/check-availability")
    public ResponseEntity<Void> checkAndNotifyAvailability(@PathVariable Long pharmacyId, @PathVariable Long drugId) {
        inventoryService.checkAndNotifyAvailability(pharmacyId, drugId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
