package com.medco.eprescription_out_of_stock.ServiceImp.Dispense;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Dispense.DispenseRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Dispense.DispensedDrugsResponseDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Dispense.PrescriptionDrugDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Dispense.PrescriptionDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescribedDrugs;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescriptions;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.ReleasedDrugs;
import com.medco.eprescription_out_of_stock.Repository.Prescription.DispensedDrugRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.DispensedDrugResponseRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescribedDrugsRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionsRepository;
import com.medco.eprescription_out_of_stock.Security.Services.UserDetailsImpl;
import com.medco.eprescription_out_of_stock.Service.Dispense.DispenseService;
import com.medco.eprescription_out_of_stock.shared.enums.prescribedDrugsEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DispenseServiceImpl implements DispenseService {


    @Autowired
    DispensedDrugResponseRepository dispensedDrugResponseRepository;

    @Autowired
    DispensedDrugRepository dispensedDrugRepository;

    @Autowired
    PrescribedDrugsRepository prescribedDrugsRepository;

    @Autowired
    PrescriptionsRepository prescriptionsRepository;

    @Override
    public ResponseEntity<?> dispenseDrug(DispenseRequestDto request) {



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String pharmacistFullName = String.join(" ",
                userDetails.getFirstName(),
                userDetails.getFatherName(),
                userDetails.getGrandFatherName()
        );


        Optional<Prescriptions> prescriptionOpt = prescriptionsRepository.findById(request.getPrescriptionId());

        if (prescriptionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid prescription ID");
        }

        if (request.getPrescribedDrugs() == null || request.getPrescribedDrugs().isEmpty()) {
            return ResponseEntity.badRequest().body("Prescribed drugs list is null or empty");
        }

        Prescriptions prescription = prescriptionOpt.get();
        List<ReleasedDrugs> releasedDrugsList = new ArrayList<>();

        for (var prescribedDrugDto : request.getPrescribedDrugs()) {
            Long prescribedDrugId = prescribedDrugDto.getPrescribedDrugId();

            Optional<PrescribedDrugs> prescribedDrugOpt = prescribedDrugsRepository.findById(prescribedDrugId);
            if (prescribedDrugOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Prescribed drug ID not found: " + prescribedDrugId);
            }

            PrescribedDrugs prescribedDrug = prescribedDrugOpt.get();
            prescribedDrug.setDespensedDate(new Date());
            prescribedDrug.setPrescribedDrugsStatus(prescribedDrugsEnum.dispensed);
            prescribedDrug.setDispensedBy(pharmacistFullName);
            prescribedDrugsRepository.save(prescribedDrug);

            ReleasedDrugs releasedDrug = new ReleasedDrugs();
            releasedDrug.setPrescribedDrug(prescribedDrug);
            releasedDrug.setPrescription(prescription);
            releasedDrugsList.add(releasedDrug);
        }

        dispensedDrugRepository.saveAll(releasedDrugsList);

        return ResponseEntity.ok("Drugs dispensed successfully");
    }





    private boolean checkAvailability(String drugId, Long quantity) {

        return  true;


    }


    @Override
    public ResponseEntity<Map<String, Object>> getAllDispensedDrugs(String searchKey, Pageable pageable) {

    return searchKey == null ? getAllDispensedDrugWithoutSearch(pageable) : getAllDispensedDrugWithSearch(searchKey, pageable);

    }

    private ResponseEntity<Map<String, Object>> getAllDispensedDrugWithSearch(String searchKey, Pageable pageable) {

        Page<ReleasedDrugs> releasedDrugs = dispensedDrugRepository.searchByPatientId(searchKey,pageable);
        List<DispensedDrugsResponseDto> responseList = releasedDrugs.getContent().stream().map(drug -> {
            DispensedDrugsResponseDto dto = new DispensedDrugsResponseDto();
            BeanUtils.copyProperties(drug, dto);
            PrescriptionDto prescription = new PrescriptionDto();
            BeanUtils.copyProperties(drug.getPrescription(), prescription);

            PrescriptionDrugDto prescribedDrug = new PrescriptionDrugDto();
            BeanUtils.copyProperties(drug.getPrescribedDrug(), prescribedDrug);

            prescription.setDrug(prescribedDrug);
            dto.setPrescription(prescription);

            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", releasedDrugs.getTotalPages());
        response.put("response", responseList);

        return ResponseEntity.ok(response);

    }


    private ResponseEntity<Map<String, Object>> getAllDispensedDrugWithoutSearch(Pageable pageable) {

        Page<ReleasedDrugs> releasedDrugs = dispensedDrugRepository.findAll(pageable);
        List<DispensedDrugsResponseDto> responseList = releasedDrugs.getContent().stream().map(drug -> {
            DispensedDrugsResponseDto dto = new DispensedDrugsResponseDto();
            BeanUtils.copyProperties(drug, dto);

            PrescriptionDto prescription = new PrescriptionDto();
            BeanUtils.copyProperties(drug.getPrescription(), prescription);

            PrescriptionDrugDto prescribedDrug = new PrescriptionDrugDto();
            BeanUtils.copyProperties(drug.getPrescribedDrug(), prescribedDrug);

            prescription.setDrug(prescribedDrug);
            dto.setPrescription(prescription);

            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", releasedDrugs.getTotalPages());
        response.put("response", responseList);

        return ResponseEntity.ok(response);

    }


    @Override
    public ResponseEntity<?> getDispensedDrugById(Long id) {

        return null;
    }

    @Override
    public ResponseEntity<?> updateDispensedDrug(Long id, DispenseRequestDto request) {

        return null;
    }


    @Override
    public ResponseEntity<Boolean> deleteDispensedDrug(Long id) {

        return null;
    }

}
