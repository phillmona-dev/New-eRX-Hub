package com.medco.eprescription_out_of_stock.Controller.Dispense;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Dispense.DispenseRequestDto;
import com.medco.eprescription_out_of_stock.Service.Dispense.DispenseService;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping ("/api/v1/erx/prescription/dispense")
public class DispenseController {

    @Autowired
    DispenseService dispenseService;

    @PostMapping("/dispenseDrug")
    public ResponseEntity<?> dispenseDrug(@RequestBody DispenseRequestDto request) {
        return dispenseService.dispenseDrug(request);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllDispensedDrugs(
            @RequestParam(value = "Search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit) {
        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

        return  dispenseService.getAllDispensedDrugs(search,pageable);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDispensedDrugById(@PathVariable Long id) {
        return dispenseService.getDispensedDrugById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDispensedDrug(
            @PathVariable Long id, @RequestBody DispenseRequestDto request) {
        return dispenseService.updateDispensedDrug(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteDispensedDrug(@PathVariable Long id) {
        return dispenseService.deleteDispensedDrug(id);
    }


}
