package com.medco.eprescription_out_of_stock.Controller.ReturnedPrescription;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.ReturnedPrescriptionRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.ReturnedPrescriptions;
import com.medco.eprescription_out_of_stock.Service.ReturnedPrescription.ReturnedPrescriptionsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/erx/prescription/returnedPrescriptions")
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedPrescriptionsController {

    @Autowired
    private ReturnedPrescriptionsService returnedPrescriptionsService;

    @PostMapping
    public ResponseEntity<?> createReturnedPrescription(@RequestBody ReturnedPrescriptionRequest request) {
        ResponseEntity<?> createdPrescription = returnedPrescriptionsService.createReturnedPrescription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrescription);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnedPrescriptions> getReturnedPrescriptionById(@PathVariable Long id) {
        ReturnedPrescriptions prescription = returnedPrescriptionsService.getReturnedPrescriptionById(id);
        if (prescription != null) {
            return ResponseEntity.ok(prescription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReturnedPrescriptions>> getAllReturnedPrescriptions() {
        List<ReturnedPrescriptions> prescriptions = returnedPrescriptionsService.getAllReturnedPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnedPrescriptions> updateReturnedPrescription(@PathVariable Long id, @RequestBody ReturnedPrescriptions updatedReturnedPrescription) {
        ReturnedPrescriptions updatedPrescription = returnedPrescriptionsService.updateReturnedPrescription(id, updatedReturnedPrescription);
        if (updatedPrescription != null) {
            return ResponseEntity.ok(updatedPrescription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnedPrescription(@PathVariable Long id) {
        returnedPrescriptionsService.deleteReturnedPrescription(id);
        return ResponseEntity.noContent().build();
    }



}
