package com.medco.eprescription_out_of_stock.Service.Patient;

import com.medco.eprescription_out_of_stock.Dto.Response.Patient.PatientRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Patient.PatientResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.Patient.PatientResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PatientService {
    ResponseEntity<PatientResponse> createPatient(PatientRequestDto req);

    PatientResponseDto getAllPatients(String searchKey, Pageable pageable);
    ResponseEntity<PatientResponse> updatePatient(Long id, PatientRequestDto req);
    ResponseEntity<PatientResponseDto> getPatientById(Long id);
    Boolean deletePatient(Long id);
    ResponseEntity<?> importInsured(MultipartFile file) throws IOException;
    ResponseEntity<?> getPatientPrescription(String id);
}
