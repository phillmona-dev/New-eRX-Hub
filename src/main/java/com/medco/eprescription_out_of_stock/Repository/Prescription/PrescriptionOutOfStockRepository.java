package com.medco.eprescription_out_of_stock.Repository.Prescription;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescriptionoutOfStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrescriptionOutOfStockRepository extends JpaRepository<PrescriptionoutOfStock, Integer> {


    @Query(value = """
        SELECT p.* FROM prescriptionout_of_stock p
        WHERE (
               p.patient_full_name::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.patient_full_name::text) OR
               p.phone_number::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.phone_number::text) OR
               p.id_number::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.id_number::text) OR
               p.prescription_number::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.prescription_number::text)
        )
        """,
        countQuery = """
        SELECT COUNT(*) FROM prescriptionout_of_stock p
        WHERE (
               p.patient_full_name::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.patient_full_name::text) OR
               p.phone_number::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.phone_number::text) OR
               p.id_number::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.id_number::text) OR
               p.prescription_number::text ILIKE COALESCE('%' || CAST(:search AS text) || '%', p.prescription_number::text)
        )
        """,
        nativeQuery = true)
    Page<PrescriptionoutOfStock> searchPrescriptions(
            @Param("search") String search,
            Pageable pageable);


    @Query(value = """
    SELECT p.* FROM prescriptionout_of_stock p 
    WHERE p.prescription_uuid = COALESCE(:identifier, p.prescription_uuid) AND 
          p.phone_number = COALESCE(:phoneNumber, p.phone_number) AND 
          (p.patient_full_name::text ILIKE COALESCE('%' || CAST(:patientName AS text) || '%', p.patient_full_name::text)) AND 
          p.id_number = COALESCE(:idNumber, p.id_number) AND 
          p.prescription_date >= COALESCE(CAST(:prescriptionDateStart AS timestamp), p.prescription_date) AND 
          p.prescription_date <= COALESCE(CAST(:prescriptionDateEnd AS timestamp), p.prescription_date)
    """,
    countQuery = """
    SELECT COUNT(*) FROM prescriptionout_of_stock p 
    WHERE p.prescription_uuid = COALESCE(:identifier, p.prescription_uuid) AND 
          p.phone_number = COALESCE(:phoneNumber, p.phone_number) AND 
          (p.patient_full_name::text ILIKE COALESCE('%' || CAST(:patientName AS text) || '%', p.patient_full_name::text)) AND 
          p.id_number = COALESCE(:idNumber, p.id_number) AND 
          p.prescription_date >= COALESCE(CAST(:prescriptionDateStart AS timestamp), p.prescription_date) AND 
          p.prescription_date <= COALESCE(CAST(:prescriptionDateEnd AS timestamp), p.prescription_date)
    """,
    nativeQuery = true)
    Page<PrescriptionoutOfStock> advancedSearch(
            @Param("identifier") String identifier,
            @Param("phoneNumber") String phoneNumber,
            @Param("patientName") String patientName,
            @Param("idNumber") String idNumber,
            @Param("prescriptionDateStart") LocalDateTime prescriptionDateStart,
            @Param("prescriptionDateEnd") LocalDateTime prescriptionDateEnd,
            Pageable pageable
    );

    List<PrescriptionoutOfStock> findByPatientId(Long patientId);
}
