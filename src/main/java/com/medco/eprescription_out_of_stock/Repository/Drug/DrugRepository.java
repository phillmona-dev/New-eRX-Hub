package com.medco.eprescription_out_of_stock.Repository.Drug;

import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug,Long> {
    List<Drug> findByDrugNameContainingIgnoreCase(String name);
}
