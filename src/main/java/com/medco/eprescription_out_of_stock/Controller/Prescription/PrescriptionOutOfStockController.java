package com.medco.eprescription_out_of_stock.Controller.Prescription;

import com.medco.eprescription_out_of_stock.annotation.RequiresApiKey;
import org.springframework.ui.Model;
import com.medco.eprescription_out_of_stock.Dto.IncomingPrescriptionDto;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionOutOfStockService;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/prescription-out-of-stock")
public class PrescriptionOutOfStockController {

    private final PrescriptionOutOfStockService prescriptionOutOfStockService;

    @Autowired
    public PrescriptionOutOfStockController(PrescriptionOutOfStockService prescriptionOutOfStockService) {
        this.prescriptionOutOfStockService = prescriptionOutOfStockService;
    }

    @RequiresApiKey
    @PostMapping("/incoming")
    public ResponseEntity<?> receiveOutOfStockPrescription(@RequestBody IncomingPrescriptionDto incomingPrescription) {
        return prescriptionOutOfStockService.processIncomingPrescription(incomingPrescription);
    }

    @PostMapping
    public ResponseEntity<?> addOutOfPrescription(@RequestBody PrescriptionOutOfStockRequest request) {
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

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> advancedSearch(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String idNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate prescriptionDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate prescriptionDateEnd,
            @PageableDefault(size = 10, sort = "prescriptionDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionOutOfStockService.advancedSearch(
                identifier, phoneNumber, patientName, idNumber, prescriptionDateStart, prescriptionDateEnd, pageable
        );
    }


    @GetMapping("/patient-prescriptions")
    public String getPatientPrescriptions(@RequestParam Long patientId, Model model) {
        List<PrescriptionOutOfStockResponse> prescriptions = prescriptionOutOfStockService.getPatientPrescriptions(patientId);
        model.addAttribute("prescriptions", prescriptions);
        return "patient-prescriptions";
    }

}
