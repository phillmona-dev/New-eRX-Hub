package com.medco.eprescription_out_of_stock.Controller.Prescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionsDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescriptions;
import com.medco.eprescription_out_of_stock.Exception.PrescriptionNotFoundException;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionsService;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/erx/prescriptions")
public class PrescriptionsController {

    @Autowired
    private PrescriptionsService prescriptionsService;

    @PostMapping("/create-prescription")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequestDto req) {

        return prescriptionsService.createPrescription(req);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionsDto> getPrescriptionById(@PathVariable Long id) {
        PrescriptionsDto prescriptionsDto = prescriptionsService.getPrescriptionById(id);
        return ResponseEntity.ok(prescriptionsDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequestDto  prescriptionsDto) {
        try {
            ResponseEntity<?> updatedPrescription = prescriptionsService.updatePrescription(id, prescriptionsDto);
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
        prescriptionsService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prescriptions-by-PharmacyId")
    public ResponseEntity<?> getPrescriptionByPharmaciyId(@RequestParam(value = "Search", required = false) String search,
                                            @RequestParam(value = "pharmacyId", required = true) Long pharmacyId,
                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "limit", defaultValue = "25") int limit)
    {


        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

        return  prescriptionsService.searchPrescriptionByPharmacyId(pharmacyId,search,pageable);

    }

//


    @GetMapping("/All-prescriptions")
    public ResponseEntity<?> getPrescription(@RequestParam(value = "Search", required = false) String search,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "limit", defaultValue = "25") int limit)
    {


        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

        return  prescriptionsService.getPrescriptons(search,pageable);

    }


    @GetMapping("/prescriptions-by-patientId")
    public ResponseEntity<?> getPrescriptionByPatientId(
                                                          @RequestParam(value = "patientId", required = true) Long patientId,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "limit", defaultValue = "25") int limit)
    {


        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

        return  prescriptionsService.searchPrescriptionByPatientId(patientId,pageable);

    }


}
