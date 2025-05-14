package com.medco.eprescription_out_of_stock.Service.Prescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescribedDrugsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrescribedDrugsService {
    PrescribedDrugsDto createPrescribedDrug(PrescriptionRequestDto req);
    PrescribedDrugsDto getPrescribedDrugById(Long id);
    List<PrescribedDrugsDto> getAllPrescribedDrugs();
    PrescribedDrugsDto updatePrescribedDrug(Long id, PrescribedDrugsDto prescribedDrugsDto);
    void deletePrescribedDrug(Long id);
}


