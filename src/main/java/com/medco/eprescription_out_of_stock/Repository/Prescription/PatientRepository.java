package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Patients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patients,Long> {


//    Page<Institution> findByFirstNameContaining(String searchKey, Pageable pageable);
}
