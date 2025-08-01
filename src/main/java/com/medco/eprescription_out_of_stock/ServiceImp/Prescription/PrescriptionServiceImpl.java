package com.medco.eprescription_out_of_stock.ServiceImp.Prescription;

import com.medco.eprescription_out_of_stock.Dto.PrescriptionDto;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.DrugPrescriptionInfoDto;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.PrescriptionRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescribedDrugsDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionResponseDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionsDto;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescribedDrugs;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;
import com.medco.eprescription_out_of_stock.Entitiy.User.UserPatientDetail;
import com.medco.eprescription_out_of_stock.Repository.Drug.DrugRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PatientRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PharmacyRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.PatientDetailRepository;
import com.medco.eprescription_out_of_stock.Security.Services.UserDetailsImpl;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionService;
import com.medco.eprescription_out_of_stock.shared.enums.PrescriptionStatus;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

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

        Pharmacy pharmacy = pharmacyRepository.findById(req.getPharmacyId())
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found with id: " + req.getPharmacyId()));

        UserPatientDetail patient = patientDetailRepository.findByUserId(req.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + req.getPatientId()));

        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(req, prescription);
        prescription.setPrescriptionDate(LocalDateTime.now());
        prescription.setPrescribedBy(physicianFullName);
        prescription.setPatientName(patient.getUser().getFirstName() + " " + patient.getUser().getFatherName());
        prescription.setPatientContact(patient.getUser().getMobilePhone());
        prescription.setStatus(PrescriptionStatus.PENDING);
        prescription.setOutOfStock(false);

        List<PrescribedDrugs> drugs = new ArrayList<>();

        for (DrugPrescriptionInfoDto prescribedDrug : req.getDrugs()) {
            Drug drug = drugRepository.findById(prescribedDrug.getDrugId())
                    .orElseThrow(() -> new EntityNotFoundException("Drug not found with ID: " + prescribedDrug.getDrugId()));

            PrescribedDrugs drugEntity = new PrescribedDrugs();
            BeanUtils.copyProperties(prescribedDrug, drugEntity);

            drugEntity.setDrug(drug);
            drugEntity.setPrescriptions(prescription);
            drugEntity.setQuantityUnit(prescribedDrug.getQuantityUnit());
            drugEntity.setTotalQuantity(prescribedDrug.getTotalQuantity());

            drugs.add(drugEntity);

            // Set the first drug name in the prescription for easier querying
            if (prescription.getDrugName() == null) {
                prescription.setDrugName(drug.getDrugName());
                prescription.setDosage(prescribedDrug.getDrugDose() + " " + prescribedDrug.getDrugDoseUnit());
                prescription.setQuantity((int) prescribedDrug.getTotalQuantity());
            }
        }

        prescription.setPrescribedDrugs(drugs);
        prescription.setPharmacy(pharmacy);
        prescription.setLocation(req.getLocation());

        Prescription createdPrescription = prescriptionRepository.save(prescription);

        return ResponseEntity.status(HttpStatus.CREATED).body("Prescribed Successfully");
    }

//
    private boolean checkAvailability(Long drugId) {
        return  true;
    }

    @Override
    public PrescriptionsDto getPrescriptionById(Long id) {

        Prescription prescription = prescriptionRepository.findById(id)
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
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        Pharmacy pharmacy = pharmacyRepository.findById(req.getPharmacyId())
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found with id: " + req.getPharmacyId()));

        BeanUtils.copyProperties(req, existingPrescription);
        existingPrescription.setPrescriptionDate(LocalDate.now().atStartOfDay());
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

        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);

        return ResponseEntity.status(HttpStatus.OK).body(updatedPrescription);


    }



    @Override
    public void deletePrescription(Long id) {

        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescriptionRepository.delete(existingPrescription);
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

        Page<Prescription> prescriptionWithPage = prescriptionRepository.findByPatientId(patientId,pageable);
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
        Page<Prescription> prescriptionWithPage = prescriptionRepository.findAll(pageable);
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


        Page<Prescription> prescriptionWithPage = prescriptionRepository.searchByPatientNameOrPhone(searchKey,pageable);
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

        Page<Prescription> prescriptionWithPage = prescriptionRepository.findByPharmacyId(pharmacyId, pageable);

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


        Page<Prescription> prescriptionWithPage = prescriptionRepository.searchByPatientNameOrPhone(searchPhoneNumber, pageable);

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

    //new

    @Override
    public Prescription logPrescription(PrescriptionDto prescriptionDto) {
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(prescriptionDto, prescription);
        return prescriptionRepository.save(prescription);
    }

}