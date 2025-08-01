package com.medco.eprescription_out_of_stock.Repository.patient;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientsRepository extends JpaRepository<Patients, Long> {
    Optional<Patients> findByPhoneNumber(String phoneNumber);
}
