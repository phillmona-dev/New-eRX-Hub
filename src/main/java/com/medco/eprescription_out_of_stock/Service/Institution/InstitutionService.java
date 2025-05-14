package com.medco.eprescription_out_of_stock.Service.Institution;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.InstitutionRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.InstitutionUpdateRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Institution;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface InstitutionService {

    ResponseEntity <?> createInstitution(InstitutionRequest institution);
    List<Institution> getAllInstitutions(String search, Pageable pageable);
    Institution getInstitutionById(Long id);
    Institution updateInstitution(Long id, InstitutionUpdateRequest institution);
    void deleteInstitution(Long id);
    ResponseEntity<?> importInstitution(MultipartFile file) throws IOException;
}
