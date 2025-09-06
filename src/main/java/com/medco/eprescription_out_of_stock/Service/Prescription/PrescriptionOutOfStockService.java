package com.medco.eprescription_out_of_stock.Service.Prescription;

import com.medco.eprescription_out_of_stock.Dto.IncomingPrescriptionDto;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.PrescriptionProcessingResponse;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionOutOfStockService {

    ResponseEntity<?> createRequest(PrescriptionOutOfStockRequest request);

    List<PrescriptionOutOfStockResponse> getAllRequests();

    PrescriptionOutOfStockRequest getRequestById(Long id);

    PrescriptionOutOfStockRequest updateStatus(Long id, String status);

    ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> searchPrescription(String search, Pageable pageable);

    ResponseEntity<PrescriptionProcessingResponse> processIncomingPrescription(IncomingPrescriptionDto incomingPrescription);

    ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> advancedSearch(String identifier, String phoneNumber, String patientName, String idNumber,
                                                                                 LocalDate prescriptionDateStart, LocalDate prescriptionDateEnd,int Page,int limit);

    List<PrescriptionOutOfStockResponse> getPatientPrescriptions(Long patientId);

    void testLocationGrouping();
}
