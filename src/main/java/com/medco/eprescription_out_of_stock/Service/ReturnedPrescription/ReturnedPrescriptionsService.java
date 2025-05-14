package com.medco.eprescription_out_of_stock.Service.ReturnedPrescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.ReturnedPrescriptionRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.ReturnedPrescriptions;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReturnedPrescriptionsService {
    ResponseEntity<?> createReturnedPrescription(ReturnedPrescriptionRequest request);
    ReturnedPrescriptions getReturnedPrescriptionById(Long id);
    List<ReturnedPrescriptions> getAllReturnedPrescriptions();
    ReturnedPrescriptions updateReturnedPrescription(Long id, ReturnedPrescriptions returnedPrescription);
    void deleteReturnedPrescription(Long id);
}
