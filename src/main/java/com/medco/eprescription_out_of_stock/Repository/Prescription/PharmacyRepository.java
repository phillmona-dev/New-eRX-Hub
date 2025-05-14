package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Long> {
    Page<Pharmacy> findByPharmacyNameContaining(String searchKey, Pageable pageable);
}
