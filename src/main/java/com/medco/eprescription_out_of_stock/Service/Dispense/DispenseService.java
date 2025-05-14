package com.medco.eprescription_out_of_stock.Service.Dispense;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Dispense.DispenseRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DispenseService {

    ResponseEntity<?> dispenseDrug(DispenseRequestDto request);
    ResponseEntity<Map<String, Object>> getAllDispensedDrugs(String searchKey, Pageable pageable);
    ResponseEntity<?> getDispensedDrugById(Long id);
    ResponseEntity<?> updateDispensedDrug(Long id, DispenseRequestDto request);
    ResponseEntity<Boolean> deleteDispensedDrug(Long id);

}
