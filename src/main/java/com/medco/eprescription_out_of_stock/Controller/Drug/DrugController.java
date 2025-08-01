package com.medco.eprescription_out_of_stock.Controller.Drug;
import com.medco.eprescription_out_of_stock.Dto.Request.Drug.DrugDtoRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Service.Drug.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    private final DrugService drugService;

    @Autowired
    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    @PostMapping
    public ResponseEntity<String> addDrug(@RequestBody DrugDtoRequest drugDtoRequest) {
        try {
            drugService.addDrug(drugDtoRequest);
            return new ResponseEntity<>("Drug added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while adding the drug", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{drugId}")
    public ResponseEntity<Drug> getDrugById(@PathVariable String drugId) {
        Drug drug = drugService.getDrugById(drugId);
        if (drug != null) {
            return new ResponseEntity<>(drug, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public ResponseEntity<List<Drug>> getAllDrugs() {
        List<Drug> drugs = drugService.getAllDrugs();
        if (!drugs.isEmpty()) {
            return new ResponseEntity<>(drugs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Drug>> searchDrugsByName(@RequestParam String name) {
        List<Drug> result = drugService.searchDrugsByName(name);
        return ResponseEntity.ok(result);
    }



}
