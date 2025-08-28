package com.medco.eprescription_out_of_stock.Controller.Pharmacy;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.pharmacy.PharmacyRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Pharmacy.PharmacyResponseDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Service.Pharmacy.PharmacyService;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/erx/prescription/pharmacy")
public class PharmacyController {
    @Autowired
    PharmacyService pharmacyService;

    @PostMapping("/create-pharmacy")
    public ResponseEntity<Pharmacy> createPharmacy(@RequestBody PharmacyRequestDto req)

    {

           return  pharmacyService.createPharmacy(req);

    }

    @GetMapping("/get-all-pharmacy")
    public PharmacyResponseDto getAllPharmacy(@RequestParam(value = "Search", required = false) String search,
                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "limit", defaultValue = "25") int limit)
      {

         Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

            return  pharmacyService.getAllPharmacy(search,pageable);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getPharmacyById(@PathVariable Long id)
    {

           return pharmacyService.getPharmacyById(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(@PathVariable Long id, @RequestBody PharmacyRequestDto req) {

        return pharmacyService.updatePharmacy(id, req);
    }

   @DeleteMapping("/{id}")
   public  ResponseEntity<?> deletePharmacy(@PathVariable Long id)
   {
       boolean isDeleted = pharmacyService.delete(id);

       if (isDeleted) {
           return ResponseEntity.ok("Pharmacy deleted successfully.");
       } else {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete pharmacy or pharmacy not found.");
       }
   }
}
