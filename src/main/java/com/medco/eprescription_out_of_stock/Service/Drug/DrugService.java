package com.medco.eprescription_out_of_stock.Service.Drug;

import com.medco.eprescription_out_of_stock.Dto.Request.Drug.DrugDtoRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DrugService {

    ResponseEntity<String> addDrug(DrugDtoRequest drugDtoRequest);

    Drug getDrugById(String drugId);

    List<Drug> getAllDrugs();

    List<Drug> searchDrugsByName(String name);
}
