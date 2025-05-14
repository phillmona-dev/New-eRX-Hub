package com.medco.eprescription_out_of_stock.Service.Prescription;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionsDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescriptions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrescriptionsService {
    ResponseEntity <?> createPrescription(PrescriptionRequestDto req);
    PrescriptionsDto getPrescriptionById(Long id);
    List<PrescriptionsDto> getAllPrescriptions();
    ResponseEntity<?> updatePrescription(Long id, PrescriptionRequestDto prescriptionsDto);
    void deletePrescription(Long id);
    ResponseEntity<?> searchPrescriptionByPharmacyId(Long pharmacyId,String search, Pageable pageable);

    ResponseEntity<?> getPrescriptons(String search, Pageable pageable);

    ResponseEntity<?> searchPrescriptionByPatientId(Long patientId,  Pageable pageable);
}


