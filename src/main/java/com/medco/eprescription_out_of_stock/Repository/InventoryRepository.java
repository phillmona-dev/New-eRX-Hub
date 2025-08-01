package com.medco.eprescription_out_of_stock.Repository;

import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Inventory;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByPharmacy(Pharmacy pharmacy);

    Inventory findByPharmacyAndDrug(Pharmacy pharmacy, Drug drug);
}
