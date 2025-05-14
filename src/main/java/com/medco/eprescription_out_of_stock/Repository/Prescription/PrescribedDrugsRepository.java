package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescribedDrugs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescribedDrugsRepository extends JpaRepository<PrescribedDrugs, Long> {
}

