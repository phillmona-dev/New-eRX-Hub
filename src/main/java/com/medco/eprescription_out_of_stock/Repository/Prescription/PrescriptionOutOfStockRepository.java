package com.medco.eprescription_out_of_stock.Repository.Prescription;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescriptionoutOfStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionOutOfStockRepository extends JpaRepository<PrescriptionoutOfStock, Long> {


    @Query(value = """
    SELECT DISTINCT p.* FROM prescriptionout_of_stock p
    LEFT JOIN medication m ON p.id = m.prescription_id
    WHERE LOWER(p.patient_full_name) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(p.phone_number) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(p.id_number) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%'))
""", nativeQuery = true)
    Page<PrescriptionoutOfStock> searchPrescriptions(@Param("search") String search, Pageable pageable);


    @Query("SELECT p FROM PrescriptionoutOfStock p WHERE " +
            "(:identifier IS NULL OR p.prescriptionUuid = :identifier) AND " +
            "(:phoneNumber IS NULL OR p.phoneNumber = :phoneNumber) AND " +
            "(:patientName IS NULL OR LOWER(p.patientFullName) LIKE LOWER(CONCAT('%', :patientName, '%'))) AND " +
            "(:idNumber IS NULL OR p.idNumber = :idNumber) AND " +
            "(:prescriptionDateStart IS NULL OR p.prescriptionDate >= :prescriptionDateStart) AND " +
            "(:prescriptionDateEnd IS NULL OR p.prescriptionDate <= :prescriptionDateEnd)")
    Page<PrescriptionoutOfStock> advancedSearch(
            @Param("identifier") String identifier,
            @Param("phoneNumber") String phoneNumber,
            @Param("patientName") String patientName,
            @Param("idNumber") String idNumber,
            @Param("prescriptionDateStart") LocalDate prescriptionDateStart,
            @Param("prescriptionDateEnd") LocalDate prescriptionDateEnd,
            Pageable pageable
    );

    List<PrescriptionoutOfStock> findByPatientId(Long patientId);
}
