package com.medco.eprescription_out_of_stock.Repository.Prescription;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;
import com.medco.eprescription_out_of_stock.shared.enums.PrescriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {


//    Prescriptions getByPatientUuid(String id);
//    Page<Prescriptions> findByPharmacyIdAndPatientIdAndFirstNameContainingOrPhoneNumberContaining(
//            Long pharmacyId, Long patientId, String firstName, String phoneNumber, Pageable pageable);

//    Page<Prescriptions> findByPharmacyIdAndPatientFirstNameContainingOrPatientPhoneNumberContaining(
//            Long pharmacyId, String firstName, String phoneNumber, Pageable pageable);
//
    Page<Prescription> findByPharmacyId(Long pharmacyId, Pageable pageable);
//
//    Page<Prescriptions> findByPatientFirstNameContainingIgnoreCaseOrPatientPhoneNumberContaining(String firstName, String phoneNumber, Pageable pageable);
//
    Page<Prescription> findByPatientId(Long patientId, Pageable pageable);

    @Query("""
    SELECT p FROM Prescription p 
    JOIN p.patient pd 
    JOIN pd.user u 
    WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchKey, '%')) 
    OR u.mobilePhone LIKE CONCAT('%', :searchKey, '%')
""")
    Page<Prescription> searchByPatientNameOrPhone(@Param("searchKey") String searchKey, Pageable pageable);

    List<Prescription> findByDrugNameAndStatus(String drugName, PrescriptionStatus status);

}

