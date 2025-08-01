package com.medco.eprescription_out_of_stock.ServiceImp.inventory;

import com.medco.eprescription_out_of_stock.Dto.inventory.InventoryDto;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Inventory;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;
import com.medco.eprescription_out_of_stock.Repository.Drug.DrugRepository;
import com.medco.eprescription_out_of_stock.Repository.InventoryRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PharmacyRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionRepository;
import com.medco.eprescription_out_of_stock.Service.inventory.InventoryService;
import com.medco.eprescription_out_of_stock.Service.notification.NotificationService;
import com.medco.eprescription_out_of_stock.shared.enums.PrescriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Inventory addInventory(Long pharmacyId, InventoryDto inventoryDto) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        Drug drug = drugRepository.findById(inventoryDto.getDrugId())
                .orElseThrow(() -> new RuntimeException("Drug not found"));

        Inventory inventory = new Inventory();
        inventory.setDrug(drug);
        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setPrice(inventoryDto.getPrice());
        inventory.setPharmacy(pharmacy);

        return inventoryRepository.save(inventory);
    }

    @Override
    public void removeInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    public List<Inventory> getPharmacyInventory(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        return inventoryRepository.findByPharmacy(pharmacy);
    }


    @Override
    public Inventory updateInventory(Long inventoryId, InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setPrice(inventoryDto.getPrice());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Check and notify availability after updating inventory
        checkAndNotifyAvailability(inventory.getPharmacy().getId(), inventory.getDrug().getId());

        return updatedInventory;
    }

    @Override
    public void checkAndNotifyAvailability(Long pharmacyId, Long drugId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        Drug drug = drugRepository.findById(drugId)
                .orElseThrow(() -> new RuntimeException("Drug not found"));

        List<Prescription> pendingPrescriptions = prescriptionRepository.findByDrugNameAndStatus(drug.getDrugName(), PrescriptionStatus.PENDING);

        for (Prescription prescription : pendingPrescriptions) {
            Inventory inventory = inventoryRepository.findByPharmacyAndDrug(pharmacy, drug);
            if (inventory != null && inventory.getQuantity() > 0) {
                prescription.setStatus(PrescriptionStatus.AVAILABLE);
                prescriptionRepository.save(prescription);
                notificationService.notifyPatient(prescription, pharmacy);
            }
        }
    }
}
