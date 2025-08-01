package com.medco.eprescription_out_of_stock.Controller.Prescription;
import com.medco.eprescription_out_of_stock.Dto.PrescriptionDto;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionsDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;
import com.medco.eprescription_out_of_stock.Exception.PrescriptionNotFoundException;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionService;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/erx/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/create-prescription")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequestDto req) {
        return prescriptionService.createPrescription(req);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionsDto> getPrescriptionById(@PathVariable Long id) {
        PrescriptionsDto prescriptionsDto = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescriptionsDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequestDto  prescriptionsDto) {
        try {
            ResponseEntity<?> updatedPrescription = prescriptionService.updatePrescription(id, prescriptionsDto);
            return ResponseEntity.ok(updatedPrescription);
        } catch (PrescriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Prescription with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred."+e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prescriptions-by-PharmacyId")
    public ResponseEntity<?> getPrescriptionByPharmaciyId(@RequestParam(value = "Search", required = false) String search,
                                            @RequestParam(value = "pharmacyId", required = true) Long pharmacyId,
                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "limit", defaultValue = "25") int limit)
    {

        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

        return  prescriptionService.searchPrescriptionByPharmacyId(pharmacyId,search,pageable);

    }

//
    @GetMapping("/All-prescriptions")
    public ResponseEntity<?> getPrescription(@RequestParam(value = "Search", required = false) String search,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "limit", defaultValue = "25") int limit) {
        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");
        return  prescriptionService.getPrescriptons(search,pageable);
    }

    @GetMapping("/prescriptions-by-patientId")
    public ResponseEntity<?> getPrescriptionByPatientId(
                                                          @RequestParam(value = "patientId", required = true) Long patientId,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "limit", defaultValue = "25") int limit)
    {
        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");
        return  prescriptionService.searchPrescriptionByPatientId(patientId,pageable);
    }

    //new

    @PostMapping("/log")
    public ResponseEntity<Prescription> logPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        Prescription loggedPrescription = prescriptionService.logPrescription(prescriptionDto);
        return new ResponseEntity<>(loggedPrescription, HttpStatus.CREATED);
    }


}
