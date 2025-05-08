package com.medco.eprescription_out_of_stock.Controller.Prescription;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionOutOfStockService;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/e-prescription/prescription-out-of-stock")
public class PrescriptionOutOfStockController {


    private final PrescriptionOutOfStockService prescriptionOutOfStockService;

    @Autowired
    public PrescriptionOutOfStockController(PrescriptionOutOfStockService prescriptionOutOfStockService) {
        this.prescriptionOutOfStockService = prescriptionOutOfStockService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionOutOfStockRequest> createRequest(
            @RequestBody PrescriptionOutOfStockRequest request) {


        return ResponseEntity.ok(prescriptionOutOfStockService.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionOutOfStockResponse>> getAllRequests() {
        return ResponseEntity.ok(prescriptionOutOfStockService.getAllRequests());
    }



    @GetMapping("/search-prescription")
    public ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> getAllClaims(
            @RequestParam(value = "Search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit) {

        Pageable pageable = PaginationUtil.paginateResource(page, limit, "id", "desc");
        return prescriptionOutOfStockService.searchPrescription(search, pageable);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionOutOfStockRequest> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionOutOfStockService.getRequestById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PrescriptionOutOfStockRequest> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(prescriptionOutOfStockService.updateStatus(id, status));
    }


}
