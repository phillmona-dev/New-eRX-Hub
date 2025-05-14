package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.ReleasedDrugs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispensedDrugRepository extends JpaRepository<ReleasedDrugs,Long> {

    Page<ReleasedDrugs> searchByPatientId(String searchKey, Pageable pageable);
}
