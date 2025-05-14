package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.ReturnedPrescriptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnedPrescriptionsRepository extends JpaRepository<ReturnedPrescriptions, String> {
}
