package com.medco.eprescription_out_of_stock.ServiceImp.Prescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.DrugPrescriptionInfoDto;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescribedDrugsDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionResponseDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionsDto;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescribedDrugs;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescriptions;
import com.medco.eprescription_out_of_stock.Entitiy.User.UserPatientDetail;
import com.medco.eprescription_out_of_stock.Repository.Drug.DrugRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PatientRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PharmacyRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionsRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.PatientDetailRepository;
import com.medco.eprescription_out_of_stock.Security.Services.UserDetailsImpl;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionsService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PrescriptionsServiceImpl implements PrescriptionsService {

    @Autowired
    private PrescriptionsRepository prescriptionsRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private DrugRepository drugRepository;


    @Autowired
    private PatientDetailRepository patientDetailRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ResponseEntity<?> createPrescription(PrescriptionRequestDto req) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String physicianFullName = String.join(" ",
                userDetails.getFirstName(),
                userDetails.getFatherName(),
                userDetails.getGrandFatherName()
        );



        Pharmacy pharmacy= pharmacyRepository.findById(req.getPharmacyId())
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found with id: " + req.getPharmacyId()));



        UserPatientDetail patient= patientDetailRepository.findByUserId(req.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found with id: " + req.getPharmacyId()));




        Prescriptions prescription=new Prescriptions();
        BeanUtils.copyProperties(req, prescription);
        prescription.setPrescriptionDate(LocalDate.now());
        prescription.setPhysicianName(physicianFullName);
        prescription.setPatient(patient);
        List<PrescribedDrugs> drugs = new ArrayList<>();


        for (DrugPrescriptionInfoDto prescribedDrug : req.getDrugs()) {


            Drug drug = drugRepository.findById(prescribedDrug.getDrugId())
                    .orElseThrow(() -> new EntityNotFoundException("Drug not found with ID: " +  prescribedDrug.getDrugId()));

            PrescribedDrugs drugEntity = new PrescribedDrugs();
                BeanUtils.copyProperties(prescribedDrug, drugEntity);

                drugEntity.setDrug(drug);
                drugEntity.setPrescriptions(prescription);

                drugs.add(drugEntity);

            }

        prescription.setPrescribedDrugs(drugs);
        prescription.setPharmacy(pharmacy);

        Prescriptions createdPrescription = prescriptionsRepository.save(prescription);

        return ResponseEntity.status(HttpStatus.CREATED).body("Prescribed Successfully");


    }

//


    private boolean checkAvailability(Long drugId) {

        return  true;

    }

    @Override
    public PrescriptionsDto getPrescriptionById(Long id) {

        Prescriptions prescription = prescriptionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        PrescriptionsDto prescriptionsDto = new PrescriptionsDto();
        prescriptionsDto.setId(prescription.getId());
        prescriptionsDto.setPharmacyName(prescription.getPharmacy().getPharmacyName());
        prescriptionsDto.setDiagnosis(prescription.getDiagnosis());
        prescriptionsDto.setChiefCompliant(prescription.getChiefCompliant());
        prescriptionsDto.setDeleted(prescription.isDeleted());

        List<PrescribedDrugsDto> prescribedDrugDtos = prescription.getPrescribedDrugs().stream()
                .map(drug -> {
                    PrescribedDrugsDto drugDto = new PrescribedDrugsDto();
                    BeanUtils.copyProperties(drug, drugDto);
                    return drugDto;
                })
                .collect(Collectors.toList());

        prescriptionsDto.setPrescribedDrug(prescribedDrugDtos);
        prescriptionsDto.setPrescribedDrug(prescribedDrugDtos);

        return prescriptionsDto;


    }





    @Override
    public List<PrescriptionsDto> getAllPrescriptions() {



     return  null;
    }



    @Override
    public ResponseEntity<?> updatePrescription(Long id, PrescriptionRequestDto req) {
        Prescriptions existingPrescription = prescriptionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        Pharmacy pharmacy = pharmacyRepository.findById(req.getPharmacyId())
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found with id: " + req.getPharmacyId()));

        BeanUtils.copyProperties(req, existingPrescription);
        existingPrescription.setPrescriptionDate(LocalDate.now());
        existingPrescription.setPharmacy(pharmacy);

// Existing prescribed drugs mapped by Drug UUID
        List<PrescribedDrugs> currentDrugs = existingPrescription.getPrescribedDrugs();
        Map<String, PrescribedDrugs> currentDrugMap = currentDrugs.stream()
                .filter(pd -> pd.getDrug() != null && pd.getDrug().getDrugUuid() != null)
                .collect(Collectors.toMap(
                        pd -> pd.getDrug().getDrugUuid(),
                        Function.identity(),
                        (existing, replacement) -> existing  // in case of duplicate UUIDs
                ));

        List<PrescribedDrugs> updatedDrugs = new ArrayList<>();

        for (DrugPrescriptionInfoDto prescribedDrug : req.getDrugs()) {
            PrescribedDrugs drugEntity;

            if (prescribedDrug.getDrugId() != null && currentDrugMap.containsKey(prescribedDrug.getDrugId())) {
                drugEntity = currentDrugMap.get(prescribedDrug.getDrugId());
                BeanUtils.copyProperties(prescribedDrug, drugEntity);
            } else {
                drugEntity = new PrescribedDrugs();
                BeanUtils.copyProperties(prescribedDrug, drugEntity);
            }

//            Drug drug = drugRepository.findAllById(Collections.singleton(prescribedDrug.getDrugId()))
//                    .set(() -> new EntityNotFoundException("Drug not found with uuid: " + prescribedDrug.getDrugId()));
//            drugEntity.setDrug(drug);
//            drugEntity.setPrescriptions(existingPrescription);
//            updatedDrugs.add(drugEntity);
        }

        existingPrescription.getPrescribedDrugs().clear();
        existingPrescription.getPrescribedDrugs().addAll(updatedDrugs);

        Prescriptions updatedPrescription = prescriptionsRepository.save(existingPrescription);

        return ResponseEntity.status(HttpStatus.OK).body(updatedPrescription);


    }



    @Override
    public void deletePrescription(Long id) {

        Prescriptions existingPrescription = prescriptionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescriptionsRepository.delete(existingPrescription);
    }



    @Override
    public ResponseEntity<?> searchPrescriptionByPharmacyId(Long pharmacyId,String searchKey, Pageable pageable) {


        return (searchKey != null && !searchKey.isEmpty())
                ? getPrescriptionByPharmacyIdWithSearch(pharmacyId,searchKey,searchKey, pageable)
                : getPrescriptionByPharmacyIdWithoutSearch(pharmacyId,pageable);

    }


    @Override
    public ResponseEntity<?> getPrescriptons(String searchKey, Pageable pageable) {
        return (searchKey != null && !searchKey.isEmpty())
                ? getAllPrescriptionWithSearch(searchKey,pageable)
                : getAllPrescriptionWithoutSearch(pageable);


    }

    @Override
    public ResponseEntity<?> searchPrescriptionByPatientId(Long patientId, Pageable pageable) {

        Page<Prescriptions> prescriptionWithPage = prescriptionsRepository.findByPatientId(patientId,pageable);
        PrescriptionResponseDto PrescriptionResponseDto = new PrescriptionResponseDto();

        List<PrescriptionResponseDto> responseList = prescriptionWithPage.getContent().stream()
                .map(prescription -> {
                    PrescriptionResponseDto ph = new PrescriptionResponseDto();

                    BeanUtils.copyProperties(prescription, ph);
                    ph.setPrescribedDrug(prescription.getPrescribedDrugs());

                    return ph;
                })

                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);




    }

    private ResponseEntity<?> getAllPrescriptionWithoutSearch(Pageable pageable) {
        Page<Prescriptions> prescriptionWithPage = prescriptionsRepository.findAll(pageable);
        PrescriptionResponseDto PrescriptionResponseDto = new PrescriptionResponseDto();

        List<PrescriptionResponseDto> responseList = prescriptionWithPage.getContent().stream()
                .map(prescription -> {
                    PrescriptionResponseDto ph = new PrescriptionResponseDto();

                    BeanUtils.copyProperties(prescription, ph);
                    ph.setPrescribedDrug(prescription.getPrescribedDrugs());

                    return ph;
                })

                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);



    }

    private ResponseEntity<?> getAllPrescriptionWithSearch(String searchKey,Pageable pageable) {


        Page<Prescriptions> prescriptionWithPage = prescriptionsRepository.searchByPatientNameOrPhone(searchKey,pageable);
        PrescriptionResponseDto PrescriptionResponseDto = new PrescriptionResponseDto();

        List<PrescriptionResponseDto> responseList = prescriptionWithPage.getContent().stream()
                .map(prescription -> {
                    PrescriptionResponseDto ph = new PrescriptionResponseDto();

                    BeanUtils.copyProperties(prescription, ph);
                    ph.setPrescribedDrug(prescription.getPrescribedDrugs());

                    return ph;
                })

                .collect(Collectors.toList());



        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);


    }


    private ResponseEntity<?> getPrescriptionByPharmacyIdWithoutSearch(Long pharmacyId ,Pageable pageable) {

        Page<Prescriptions> prescriptionWithPage = prescriptionsRepository.findByPharmacyId(pharmacyId, pageable);

        PrescriptionResponseDto PrescriptionResponseDto = new PrescriptionResponseDto();

        List<PrescriptionResponseDto> responseList = prescriptionWithPage.getContent().stream()
                .map(prescription -> {
                    PrescriptionResponseDto ph = new PrescriptionResponseDto();

                    BeanUtils.copyProperties(prescription, ph);
                    ph.setPrescribedDrug(prescription.getPrescribedDrugs());

                    return ph;
                })

                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }



    private ResponseEntity<?>  getPrescriptionByPharmacyIdWithSearch(long pharmacyId,String searchFirstName,String searchPhoneNumber, Pageable pageable) {


        Page<Prescriptions> prescriptionWithPage = prescriptionsRepository.searchByPatientNameOrPhone(searchPhoneNumber, pageable);

        PrescriptionResponseDto PrescriptionResponseDto = new PrescriptionResponseDto();

        List<PrescriptionResponseDto> responseList = prescriptionWithPage.getContent().stream()
                .map(prescription -> {
                    PrescriptionResponseDto ph = new PrescriptionResponseDto();

                    BeanUtils.copyProperties(prescription, ph);
                    ph.setPrescribedDrug(prescription.getPrescribedDrugs());

                    return ph;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);


    }

}