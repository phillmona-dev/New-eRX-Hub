package com.medco.eprescription_out_of_stock.Service.Prescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrescriptionOutOfStockService {

    PrescriptionOutOfStockRequest createRequest(PrescriptionOutOfStockRequest request);
    List<PrescriptionOutOfStockResponse> getAllRequests();
    PrescriptionOutOfStockRequest getRequestById(Long id);
    PrescriptionOutOfStockRequest updateStatus(Long id, String status);

    ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> searchPrescription(String search, Pageable pageable);
}
