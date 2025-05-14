package com.medco.eprescription_out_of_stock.Controller.Institution;


import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.InstitutionRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.InstitutionUpdateRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Institution;
import com.medco.eprescription_out_of_stock.Service.Institution.InstitutionService;
import com.medco.eprescription_out_of_stock.Utills.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/erx/prescription/institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    @Autowired
    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    // Create a new institution
    @PostMapping
    public ResponseEntity<?> createInstitution(@RequestBody InstitutionRequest institution) {
        ResponseEntity<?> createdInstitution = institutionService.createInstitution(institution);
        return ResponseEntity.ok(createdInstitution);
    }

    @GetMapping
    public ResponseEntity<List<Institution>> getInstitutionsWithSearchAndWithOutSearch(@RequestParam(value = "Search", required = false) String search,
                                                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                       @RequestParam(value = "limit", defaultValue = "25") int limit) {

        Pageable pageable = PaginationUtil.paginateResource(page,limit,"id","desc");

        List<Institution> institutions = institutionService.getAllInstitutions(search,pageable);
        return ResponseEntity.ok(institutions);


    }

    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable Long id) {
        Institution institution = institutionService.getInstitutionById(id);
        return ResponseEntity.ok(institution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Institution> updateInstitution(@PathVariable Long id, @RequestBody InstitutionUpdateRequest institution) {
        Institution updatedInstitution = institutionService.updateInstitution(id, institution);
        return ResponseEntity.ok(updatedInstitution);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Long id) {

        institutionService.deleteInstitution(id);
        return ResponseEntity.noContent().build();


    }


    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importInsuredPersons(
            @RequestParam("file") MultipartFile file

    ) throws IOException {
        return institutionService.importInstitution(file);
    }



}
