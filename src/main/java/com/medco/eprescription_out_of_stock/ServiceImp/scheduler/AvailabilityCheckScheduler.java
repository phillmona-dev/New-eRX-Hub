package com.medco.eprescription_out_of_stock.ServiceImp.scheduler;

import com.medco.eprescription_out_of_stock.Service.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityCheckScheduler {

    @Autowired
    private InventoryService inventoryService;

    @Scheduled(fixedRate = 3600000) // Run every hour (3600000 milliseconds)
    public void checkAvailability() {
        // This method will be called every hour
        // It should check all pharmacies and drugs
        // For simplicity, we'll just log a message here
        System.out.println("Running scheduled availability check");
        // In a real implementation, you would iterate through all pharmacies and drugs
        // and call inventoryService.checkAndNotifyAvailability for each combination
    }
}
