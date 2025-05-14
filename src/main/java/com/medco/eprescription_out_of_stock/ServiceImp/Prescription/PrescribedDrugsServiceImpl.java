package com.medco.eprescription_out_of_stock.ServiceImp.Prescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescribedDrugsDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescribedDrugs;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescribedDrugsRepository;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescribedDrugsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescribedDrugsServiceImpl implements PrescribedDrugsService {

    @Autowired
    private PrescribedDrugsRepository prescribedDrugsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PrescribedDrugsDto createPrescribedDrug(PrescriptionRequestDto req) {
        PrescribedDrugs prescribedDrug = modelMapper.map(req, PrescribedDrugs.class);
        PrescribedDrugs savedPrescribedDrug = prescribedDrugsRepository.save(prescribedDrug);
        return modelMapper.map(savedPrescribedDrug, PrescribedDrugsDto.class);
    }

    @Override
    public PrescribedDrugsDto getPrescribedDrugById(Long id) {
        PrescribedDrugs prescribedDrug = prescribedDrugsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PrescribedDrug not found"));
        return modelMapper.map(prescribedDrug, PrescribedDrugsDto.class);
    }

    @Override
    public List<PrescribedDrugsDto> getAllPrescribedDrugs() {
        List<PrescribedDrugs> prescribedDrugs = prescribedDrugsRepository.findAll();
        return prescribedDrugs.stream()
                .map(prescribedDrug -> modelMapper.map(prescribedDrug, PrescribedDrugsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PrescribedDrugsDto updatePrescribedDrug(Long id, PrescribedDrugsDto prescribedDrugsDto) {
        PrescribedDrugs existingPrescribedDrug = prescribedDrugsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PrescribedDrug not found"));
        modelMapper.map(prescribedDrugsDto, existingPrescribedDrug);
        PrescribedDrugs updatedPrescribedDrug = prescribedDrugsRepository.save(existingPrescribedDrug);
        return modelMapper.map(updatedPrescribedDrug, PrescribedDrugsDto.class);
    }

    @Override
    public void deletePrescribedDrug(Long id) {
        PrescribedDrugs existingPrescribedDrug = prescribedDrugsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PrescribedDrug not found"));
        prescribedDrugsRepository.delete(existingPrescribedDrug);
    }
}
