package com.medco.eprescription_out_of_stock.ServiceImp.Patient;

import com.medco.eprescription_out_of_stock.Dto.Response.Patient.PatientRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Patient.PatientResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.Patient.PatientResponseDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Patients;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PatientRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionRepository;
import com.medco.eprescription_out_of_stock.Service.Patient.PatientService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImp implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Override
    public ResponseEntity<PatientResponse> createPatient(PatientRequestDto req) {

        Patients patient = new Patients();

        BeanUtils.copyProperties(req, patient);
        Patients savedPatient = patientRepository.save(patient);
        PatientResponse response = new PatientResponse();
        BeanUtils.copyProperties(savedPatient, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public PatientResponseDto getAllPatients(String searchKey, Pageable pageable) {

        return (searchKey != null && !searchKey.isEmpty())
                ? getAllPatientsWithSearch(searchKey, pageable)
                : getAllPatientsWithOutSearch(pageable);
    }

    private PatientResponseDto getAllPatientsWithOutSearch(Pageable pageable) {
        Page<Patients> patientsWithPage = patientRepository.findAll(pageable);
        PatientResponseDto patientResponseDto = new PatientResponseDto();
        patientResponseDto.setTotalPage((long) patientsWithPage.getTotalPages());

        List<PatientResponse> responseList = patientsWithPage.getContent().stream()
                .map(patient -> {
                    PatientResponse pr = new PatientResponse();
                    BeanUtils.copyProperties(patient, pr);
                    return pr;
                })
                .collect(Collectors.toList());

        patientResponseDto.setResponse(responseList);
        return patientResponseDto;


    }

    private PatientResponseDto getAllPatientsWithSearch(String searchKey, Pageable pageable) {

//        Page<Patients> patientsWithPage = patientRepository.findByFirstNameContaining(searchKey, pageable);
//        PatientResponseDto patientResponseDto = new PatientResponseDto();
//        patientResponseDto.setTotalPage((long) patientsWithPage.getTotalPages());

//        List<PatientResponse> responseList = patientsWithPage.getContent().stream()
//                .map(patient -> {
//                    PatientResponse pr = new PatientResponse();
//                    BeanUtils.copyProperties(patient, pr);
//                    return pr;
//                })
//                .collect(Collectors.toList());
//
//        patientResponseDto.setResponse(responseList);
//        return patientResponseDto;
        return null;


    }

    @Override
    public ResponseEntity<PatientResponse> updatePatient(Long id, PatientRequestDto req) {

        Optional<Patients> existingPatient = patientRepository.findById(id);

        if (existingPatient.isPresent()) {
            Patients patientToUpdate = existingPatient.get();
            BeanUtils.copyProperties(req, patientToUpdate, "id");
            Patients updatedPatient = patientRepository.save(patientToUpdate);
            PatientResponse response = new PatientResponse();
            BeanUtils.copyProperties(updatedPatient, response);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<PatientResponseDto> getPatientById(Long id) {

        Optional<Patients> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            PatientResponseDto responseDto = new PatientResponseDto();
            Patients patient = optionalPatient.get();
            BeanUtils.copyProperties(patient, responseDto);

            return ResponseEntity.ok(responseDto);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Boolean deletePatient(Long id) {
        Optional<Patients> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            patientRepository.delete(optionalPatient.get());
            return true;
        } else {
            return false;
        }
    }



    @Override
    public ResponseEntity<?> getPatientPrescription(String id) {

//        Prescriptions patientPrescription = prescriptionsRepository.findByPatientId(id);
//        if (patientPrescription == null) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body("Prescription not found for patient ID: " + id);
//        }
//
//        return ResponseEntity.ok(patientPrescription);


        return null;
    }



    //    importing file
    @Override
    public ResponseEntity<?> importInsured(MultipartFile file) throws IOException {

        List<Patients> patients = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(convert(file));
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    validateHeaderRow(row);
                    continue;
                }

                Patients patient = mapToPatientEntity(row);
                patients.add(patient);
            }


            String successMessage= savePatientsFromExcel(patients);

            return ResponseEntity.ok(successMessage);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error importing patients: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error processing request: " + e.getMessage());
        }


    }


    @Transactional
    public String savePatientsFromExcel(List<Patients> patients) {
        List<Patients> batch = new ArrayList<>();
        int batchSize = 100;
//        Logger logger = LoggerFactory.getLogger(PatientService.class);

        try {
            for (int i = 0; i < patients.size(); i++) {
                batch.add(patients.get(i));
                if (batch.size() == batchSize || i == patients.size() - 1) {
                    patientRepository.saveAll(batch);
                    batch.clear();
                }
            }
        } catch (Exception e) {
//            logger.error("Error saving patients: {}", e.getMessage(), e);
            return "Error occurred while importing patients: " + e.getMessage();
        }

        return "Imported successfully";
    }



    private Patients mapToPatientEntity(Row row) {

        Patients patient=new Patients();
        patient.setFirstName(String.valueOf(row.getCell(0)));
        patient.setLastName(String.valueOf(row.getCell(1)));
        patient.setGrandFatherName(String.valueOf(row.getCell(2)));
        patient.setGender(String.valueOf(row.getCell(3)));
        patient.setInsuranceId(Long.parseLong(String.valueOf(row.getCell(4))));
        patient.setKebelle(String.valueOf(row.getCell(5)));
        patient.setWoreda(String.valueOf(row.getCell(6)));


        return patient;


    }


    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    private void validateHeaderRow(Row row) {
        String[] headers = {
                "First Name", "Last Name", "Grandfather Name", "Date of Birth", "Gender", "Id Type", "Id Number", "Phone Number", "House Number", "Insurance Name"
        };



        for (int i = 0; i < headers.length; i++) {
            System.out.println("no error ocurred");


            if (!row.getCell(i).getStringCellValue().equalsIgnoreCase(headers[i])) {
                throw new BadRequestException("The Excel sheet for uploading Insured Members should use the format given.");
            }
        }


    }


//   end of importing



}
