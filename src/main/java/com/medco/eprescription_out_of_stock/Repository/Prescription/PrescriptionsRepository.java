package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionsRepository extends JpaRepository<Prescriptions, Long> {


//    Prescriptions getByPatientUuid(String id);
//    Page<Prescriptions> findByPharmacyIdAndPatientIdAndFirstNameContainingOrPhoneNumberContaining(
//            Long pharmacyId, Long patientId, String firstName, String phoneNumber, Pageable pageable);

//    Page<Prescriptions> findByPharmacyIdAndPatientFirstNameContainingOrPatientPhoneNumberContaining(
//            Long pharmacyId, String firstName, String phoneNumber, Pageable pageable);
//
    Page<Prescriptions> findByPharmacyId(Long pharmacyId, Pageable pageable);
//
//    Page<Prescriptions> findByPatientFirstNameContainingIgnoreCaseOrPatientPhoneNumberContaining(String firstName, String phoneNumber, Pageable pageable);
//
    Page<Prescriptions> findByPatientId(Long patientId, Pageable pageable);

    @Query("""
    SELECT p FROM Prescriptions p 
    JOIN p.patient pd 
    JOIN pd.user u 
    WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchKey, '%')) 
    OR u.mobilePhone LIKE CONCAT('%', :searchKey, '%')
""")
    Page<Prescriptions> searchByPatientNameOrPhone(@Param("searchKey") String searchKey, Pageable pageable);

}

