package com.medco.eprescription_out_of_stock.ServiceImp.Prescription;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medco.eprescription_out_of_stock.Dto.IncomingPrescriptionDto;
import com.medco.eprescription_out_of_stock.Dto.OptMessage.OtpResponse;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.MedicineList;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Entitiy.ExternalSystemAuditLog;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Medication;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Patients;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescriptionoutOfStock;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionOutOfStockRepository;
import com.medco.eprescription_out_of_stock.Repository.patient.PatientsRepository;
import com.medco.eprescription_out_of_stock.Service.ExternalSystemAuditService;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionOutOfStockService;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import com.medco.eprescription_out_of_stock.shared.enums.ExternalSystemType;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@Slf4j
public class PrescriptionOutOfStockServiceImpl implements PrescriptionOutOfStockService {


    @Value("${afromessage.api.url}")
    private String baseURL;
    @Value("${afromessage.api.token}")
    private String token;
    @Value("${afromessage.api.identifierId}")
    private String identifierId;

    @Value("${kenema.api.url}")
    private String kenemaUrl;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PatientsRepository patientsRepository;
    private final PrescriptionOutOfStockRepository prescriptionOutOfStockRepository;
    private final ExternalSystemAuditService auditService;

    public PrescriptionOutOfStockServiceImpl(PatientsRepository patientsRepository,
                                           PrescriptionOutOfStockRepository prescriptionOutOfStockRepository,
                                           ExternalSystemAuditService auditService) {
        this.patientsRepository = patientsRepository;
        this.prescriptionOutOfStockRepository = prescriptionOutOfStockRepository;
        this.auditService = auditService;
    }

    @Override
    public ResponseEntity<OtpResponse> createRequest(PrescriptionOutOfStockRequest request) {

        List<String> medicineNames = request.getMedicineLists()
                .stream()
                .map(MedicineList::getName)
                .collect(Collectors.toList());

        String phoneNumber = request.getPatientDetail().getPhoneNumber();

        String stockResponseBody = searchMedcineFromStock(phoneNumber, medicineNames);

        if (stockResponseBody == null || stockResponseBody.isEmpty()) {
            OtpResponse otpResponse = new OtpResponse(false, "Oops! Something went wrong during medicine stock check or OTP send.");
            return ResponseEntity.badRequest().body(otpResponse);
        }

        PrescriptionoutOfStock prescription = new PrescriptionoutOfStock();
        BeanUtils.copyProperties(request.getPatientDetail(), prescription);

        List<Medication> medications = request.getMedicineLists().stream().map(medReq -> {
            Medication med = new Medication();
            med.setName(medReq.getName());
            med.setUnit(medReq.getUnit());
            med.setQuantity(medReq.getQuantity());
            med.setDescription(medReq.getDescription());
            med.setTotalPrice(medReq.getTotalPrice());
            med.setPrescription(prescription);
            return med;
        }).toList();

        prescription.setMedications(medications);
        prescriptionOutOfStockRepository.save(prescription);

        return ResponseEntity.ok(new OtpResponse(true, "OTP sent successfully with medicine location and prescription saved."));
    }

    private String searchMedcineFromStock(String phoneNumber, List<String> medicineNames) {

        try {

            String jsonBody = objectMapper.writeValueAsString(medicineNames);

            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request searchRequest = new Request.Builder()
                    .url(kenemaUrl + "/mock-medicine-locations")
                    .post(body)
                    .build();

            try (Response searchResponse = httpClient.newCall(searchRequest).execute()) {
                if (!searchResponse.isSuccessful()) {
                    System.err.println("Failed to search medicines. Response code: " + searchResponse.code());
                    return null;
                }

                String stockResponse = searchResponse.body().string();
                System.out.println("Stock Response: " + stockResponse);

                List<Map<String, String>> stockInfo = objectMapper.readValue(
                        stockResponse, new TypeReference<List<Map<String, String>>>() {}
                );

                StringBuilder messageBuilder = new StringBuilder("Your OTP is: ");
                int otp = 1000 + new SecureRandom().nextInt(9000);
                messageBuilder.append(otp).append("\n");

//            [
//                {"medicine": "Paracetamol", "location": "Main Store"},
//                {"medicine": "Ibuprofen", "location": "Branch A"}
//            ]

                for (Map<String, String> entry : stockInfo) {
                    String medicine = entry.get("medicine");
                    String location = entry.get("location");
                    messageBuilder.append(" - ").append(medicine).append(": ").append(location).append("\n");
                }

                HttpUrl url = HttpUrl.parse(baseURL).newBuilder()
                        .addQueryParameter("to", phoneNumber)
                        .addQueryParameter("message", messageBuilder.toString())
                        .addQueryParameter("from", identifierId)
                        .build();

                Request otpRequest = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + token)
                        .build();

                try (Response otpResponse = httpClient.newCall(otpRequest).execute()) {
                    if (!otpResponse.isSuccessful()) {
                        System.err.println("OTP send failed. Code: " + otpResponse.code());
                        return null;
                    }
                    return stockResponse;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PrescriptionOutOfStockResponse> getAllRequests() {

        List<PrescriptionoutOfStock> prescriptions = prescriptionOutOfStockRepository.findAll();
        return prescriptions.stream().map(prescription -> {
            PrescriptionOutOfStockResponse response = new PrescriptionOutOfStockResponse();
            response.setPatientFullName(prescription.getPatientFullName());
            response.setGender(prescription.getGender());
            response.setAge(prescription.getAge());
            response.setPhoneNumber(prescription.getPhoneNumber());
            response.setHouseNumber(prescription.getHouseNumber());
            response.setIdNumber(prescription.getIdNumber());
            response.setInsuranceNumber(prescription.getInsuranceNumber());
            response.setAddress(prescription.getAddress());
            response.setRegion(prescription.getRegion());
            response.setKebele(prescription.getKebele());
            response.setWoreda(prescription.getWoreda());
            response.setCity(prescription.getCity());
            response.setWeight(prescription.getWeight());

            List<MedicineList> medicineLists = prescription.getMedications().stream().map(med -> {
                MedicineList medDto = new MedicineList();
                medDto.setName(med.getName());
                medDto.setUnit(med.getUnit());
                medDto.setQuantity(med.getQuantity());
                medDto.setDescription(med.getDescription());
                medDto.setTotalPrice(med.getTotalPrice());
                return medDto;
            }).toList();

            response.setMedicineLists(medicineLists);
            return response;
        }).toList();

    }

    @Override
    public PrescriptionOutOfStockRequest getRequestById(Long id) {
        return null;
    }

    @Override
    public PrescriptionOutOfStockRequest updateStatus(Long id, String status) {
        return null;
    }

    @Override
    public ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> searchPrescription(String search, Pageable pageable) {

        Page<PrescriptionoutOfStock> prescriptionPage = prescriptionOutOfStockRepository
                .searchPrescriptions(
                        search, pageable
                );

        System.out.println("prescription after search" + prescriptionPage.getTotalPages());

        List<PrescriptionOutOfStockResponse> responseList = prescriptionPage.getContent().stream().map(prescription -> {
            PrescriptionOutOfStockResponse response = new PrescriptionOutOfStockResponse();
            response.setPatientFullName(prescription.getPatientFullName());
            response.setGender(prescription.getGender());
            response.setAge(prescription.getAge());
            response.setPhoneNumber(prescription.getPhoneNumber());
            response.setHouseNumber(prescription.getHouseNumber());
            response.setIdNumber(prescription.getIdNumber());
            response.setInsuranceNumber(prescription.getInsuranceNumber());
            response.setAddress(prescription.getAddress());
            response.setRegion(prescription.getRegion());
            response.setKebele(prescription.getKebele());
            response.setWoreda(prescription.getWoreda());
            response.setCity(prescription.getCity());
            response.setWeight(prescription.getWeight());

            List<MedicineList> medicines = prescription.getMedications().stream().map(med -> {
                MedicineList medDto = new MedicineList();
                medDto.setName(med.getName());
                medDto.setUnit(med.getUnit());
                medDto.setQuantity(med.getQuantity());
                medDto.setDescription(med.getDescription());
                medDto.setTotalPrice(med.getTotalPrice());
                return medDto;
            }).toList();

            response.setMedicineLists(medicines);
            return response;
        }).toList();


        PagedResponse<PrescriptionOutOfStockResponse> response = new PagedResponse<>(
                prescriptionPage.getTotalPages(),
//                prescriptionPage.getTotalElements(),
                responseList
        );

        return ResponseEntity.ok(response);

    }

    @Override
    @Transactional
    public ResponseEntity<?> processIncomingPrescription(IncomingPrescriptionDto incomingPrescription) {

        log.info("Starting to process incoming prescription");

        log.info("Searching for patient with phone number: {}", incomingPrescription.getPatient().getPhoneNumber());
        Patients patient = patientsRepository.findByPhoneNumber(incomingPrescription.getPatient().getPhoneNumber())
                .orElse(new Patients());
        log.info("Patient found: {}", patient.getId() != null ? "Yes" : "No, creating new patient");

        log.info("Updating patient information");
        BeanUtils.copyProperties(incomingPrescription.getPatient(), patient, "id", "patientUuid");
        patient.setGrandFatherName(incomingPrescription.getPatient().getMiddleName());
        patient.setGender(incomingPrescription.getPatient().getSex());
        patient.setDateOfBirth(calculateDateOfBirth(incomingPrescription.getPatient().getAge(), incomingPrescription.getPatient().getAgeType()));
        patient.setIdNumber(incomingPrescription.getPatient().getCardNumber());
        patient.setKebelle(incomingPrescription.getPatient().getKebele());
        patient.setEmployerName(incomingPrescription.getPatient().getSponsorName());
        patient.setWoreda(incomingPrescription.getPatient().getWoredaId().toString());

        log.info("Saving updated patient information");
        Patients savedPatient = patientsRepository.save(patient);
        log.info("Patient saved with ID: {}", savedPatient.getId());

        log.info("Creating new PrescriptionoutOfStock");
        PrescriptionoutOfStock prescription = new PrescriptionoutOfStock();
        prescription.setPatient(savedPatient);
        prescription.setPrescriptionUuid(incomingPrescription.getPrescriptionUUID().toString());

        log.info("Setting prescription details");
        BeanUtils.copyProperties(incomingPrescription.getPatient(), prescription, "id");
        prescription.setPatientFullName(String.join(" ",
                incomingPrescription.getPatient().getFirstName(),
                incomingPrescription.getPatient().getMiddleName(),
                incomingPrescription.getPatient().getLastName()));
        prescription.setGender(incomingPrescription.getPatient().getSex());
        prescription.setKebele(incomingPrescription.getPatient().getKebele());
        prescription.setCardNumber(incomingPrescription.getPatient().getCardNumber());
        prescription.setWoredaId(incomingPrescription.getPatient().getWoredaId().toString());

        prescription.setPrescriberName(String.join(" ",
                incomingPrescription.getPrescriber().getFirstName(),
                incomingPrescription.getPrescriber().getMiddleName(),
                incomingPrescription.getPrescriber().getLastName()));
        prescription.setPrescriberQualification(incomingPrescription.getPrescriber().getQualification());
        prescription.setPrescriberRegistrationNumber(incomingPrescription.getPrescriber().getRegistrationNumber());

        BeanUtils.copyProperties(incomingPrescription, prescription, "id", "patient", "medications", "diagnosis");
        prescription.setInstitutionId(incomingPrescription.getInstitutionId().toString());

        log.info("Processing medications");
        List<Medication> medications = incomingPrescription.getPrescriptionDetails().stream()
                .map(detail -> {
                    Medication med = new Medication();
                    BeanUtils.copyProperties(detail, med, "id");
                    med.setAdministrationId(detail.getAdministrationId().toString());
                    med.setName(detail.getMedicationName());
                    med.setFrequencyTypeId(detail.getFrequencyTypeId().toString());
                    med.setItemUnitId(detail.getItemUnitId().toString());
                    med.setPrescription(prescription);
                    log.info("Processed medication: {}", med.getName());
                    return med;
                })
                .collect(Collectors.toList());

        prescription.setMedications(medications);

        log.info("Processing diagnosis");
        List<String> diagnosisList = incomingPrescription.getPrescriptionDiagnosis().stream()
                .map(diagnosis -> diagnosis.getDiagnosisTypeId() + ": " + diagnosis.getAdditionalInfo())
                .collect(Collectors.toList());
        prescription.setDiagnosis(String.join(", ", diagnosisList));

        log.info("Saving prescription");
        PrescriptionoutOfStock savedPrescription = prescriptionOutOfStockRepository.save(prescription);
        log.info("Prescription saved with ID: {}", savedPrescription.getId());

        String uniqueIdentifier = generateUniqueIdentifier(savedPrescription);
        log.info("Generated unique identifier: {}", uniqueIdentifier);

        log.info("Building message for patient");
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Your prescription has been recorded as out of stock. ")
                .append("Your unique identifier is: ").append(uniqueIdentifier).append(". ")
                .append("Please use this identifier when inquiring about your prescription.\n\n");

        messageBuilder.append("Medicine availability:\n");

        log.info("Checking medicine availability");

        // Collect all medicine availability data first - grouped by location
        Map<String, Map<String, String>> locationMedicineMap = new LinkedHashMap<>();
        boolean allMedicinesUnavailable = true;

        for (Medication medication : medications) {
            log.info("Searching stock for medication: {}", medication.getName());
            List<Map<String, Object>> availabilityInfo = searchMedicineFromStock(medication.getName());

            if (!availabilityInfo.isEmpty()) {
                allMedicinesUnavailable = false;

                for (Map<String, Object> info : availabilityInfo) {
                    String branchName = (String) info.get("branchName");
                    String availability = (String) info.get("availableAmount");

                    if (branchName != null && availability != null && !"Out of Stock".equals(availability)) {
                        locationMedicineMap.computeIfAbsent(branchName, k -> new LinkedHashMap<>())
                                .put(medication.getName(), availability);
                    }
                }
                log.info("Found availability information for {}", medication.getName());
            } else {
                log.info("No availability information found for {}", medication.getName());
            }
        }

        // Build location-based message
        if (!allMedicinesUnavailable && !locationMedicineMap.isEmpty()) {
            // Sort locations by number of available medicines (descending) and limit to 3
            List<Map.Entry<String, Map<String, String>>> sortedLocations = locationMedicineMap.entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                    .limit(3)
                    .collect(Collectors.toList());

            sortedLocations.forEach(locationEntry -> {
                String location = locationEntry.getKey();
                Map<String, String> medicines = locationEntry.getValue();

                messageBuilder.append(location).append(":\n");
                medicines.forEach((medicineName, availability) ->
                        messageBuilder.append("  ").append(medicineName)
                                .append(": ").append(availability).append("\n"));
                messageBuilder.append("\n");
            });

            // Add recommendation using the first entry from sorted list
            if (!sortedLocations.isEmpty()) {
                String topLocation = sortedLocations.get(0).getKey();
                int medicineCount = sortedLocations.get(0).getValue().size();
                messageBuilder.append("Recommendation: Visit ").append(topLocation)
                        .append(" where ").append(medicineCount)
                        .append(" of your medications are available.\n");
            }
        }

        if (allMedicinesUnavailable) {
            messageBuilder.append("\nWe apologize, but we were unable to check the availability of your medications at this time. Please try again later or contact your healthcare provider for alternatives.");
        }

        log.info("Sending message to patient");
        boolean messageSent = sendMessageToPatient(savedPrescription.getPhoneNumber(), messageBuilder.toString());

        if (messageSent) {
            log.info("Message sent successfully to patient");
            return ResponseEntity.ok("Prescription processed successfully. Unique Identifier: " + uniqueIdentifier);
        } else {
            log.error("Failed to send message to patient");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Prescription saved but failed to send message to patient.");
        }
    }

    private List<Map<String, Object>> searchMedicineFromStock(String drugName) {
        return searchMedicineFromStock(drugName, null, null);
    }

    private List<Map<String, Object>> searchMedicineFromStock(String drugName, String prescriptionUuid, String patientPhone) {
        log.info("Searching for medicine in stock: {}", drugName);

        String url = UriComponentsBuilder.fromHttpUrl(kenemaUrl + "/api/kenema/v1/stock/inventory/cross-branch-availability")
                .queryParam("search", drugName)
                .queryParam("page", 1)
                .queryParam("limit", 25)
                .queryParam("sortBy", "id")
                .queryParam("sortDirection", "desc")
                .toUriString();

        log.debug("Constructed URL for stock search: {}", url);

        ExternalSystemAuditLog auditLog = auditService.createAuditLog(
                ExternalSystemType.KENEMA_STOCK_API,
                "SEARCH_MEDICINE_STOCK",
                url,
                String.format("{\"drugName\":\"%s\"}", drugName),
                prescriptionUuid,
                patientPhone,
                drugName,
                null
        );

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            log.debug("Received response with status code: {}", response.code());

            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                log.error("Failed to search medicines. Response code: {}", response.code());
                auditService.markAsFailed(auditLog.getId(),
                        String.format("HTTP %d: Failed to search medicines", response.code()),
                        response.code());
                return Collections.emptyList();
            }

            log.debug("Received response body: {}", responseBody);
            auditService.markAsSuccess(auditLog.getId(), responseBody, response.code());

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode contentNode = rootNode.get("content");

            if (contentNode != null && contentNode.isArray()) {
                log.info("Successfully parsed medicine stock information for: {}", drugName);
                List<Map<String, Object>> result = objectMapper.convertValue(contentNode, new TypeReference<List<Map<String, Object>>>() {});

                List<Map<String, Object>> enhancedResult = result.stream()
                        .map(this::enhanceMedicineAvailabilityInfo)
                        .collect(Collectors.toList());

                log.debug("Parsed {} stock entries for {}", enhancedResult.size(), drugName);
                return enhancedResult;
            } else {
                log.warn("No content found in the response for drug: {}", drugName);
            }
        } catch (SocketTimeoutException e) {
            log.error("Connection timed out while searching for medicine stock: {}", drugName, e);
            auditService.markAsTimeout(auditLog.getId());
        } catch (IOException e) {
            log.error("Error occurred while searching for medicine stock: {}", drugName, e);
            auditService.markAsFailed(auditLog.getId(), "IOException: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error occurred while processing medicine stock search for: {}", drugName, e);
            auditService.markAsFailed(auditLog.getId(), "Unexpected error: " + e.getMessage(), null);
        }

        log.warn("Returning empty list as no stock information found for: {}", drugName);
        return Collections.emptyList();
    }

    private Map<String, Object> enhanceMedicineAvailabilityInfo(Map<String, Object> originalInfo) {
        Map<String, Object> enhanced = new java.util.HashMap<>(originalInfo);

        Object availableAmountObj = originalInfo.get("availableAmount");
        if (availableAmountObj != null) {
            try {
                int availableAmount = Integer.parseInt(availableAmountObj.toString());
                String availabilityStatus = getAvailabilityStatus(availableAmount);
                enhanced.put("availableAmount", availabilityStatus);
                enhanced.put("stockLevel", getStockLevel(availableAmount));
            } catch (NumberFormatException e) {
                log.warn("Could not parse available amount: {}", availableAmountObj);
                enhanced.put("availableAmount", "Unknown");
                enhanced.put("stockLevel", "UNKNOWN");
            }
        } else {
            enhanced.put("availableAmount", "Not Available");
            enhanced.put("stockLevel", "OUT_OF_STOCK");
        }

        return enhanced;
    }

    private String getAvailabilityStatus(int quantity) {
        if (quantity == 0) {
            return "Out of Stock";
        } else if (quantity <= 5) {
            return "Limited Stock";
        } else if (quantity <= 10) {
            return "Low Stock";
        } else {
            return "Available (10+)";
        }
    }

    private String getStockLevel(int quantity) {
        if (quantity == 0) {
            return "OUT_OF_STOCK";
        } else if (quantity <= 5) {
            return "LIMITED";
        } else if (quantity <= 10) {
            return "LOW";
        } else {
            return "ADEQUATE";
        }
    }

    private boolean sendMessageToPatient(String phoneNumber, String message) {
        HttpUrl url = HttpUrl.parse(baseURL).newBuilder()
                .addQueryParameter("to", phoneNumber)
                .addQueryParameter("message", message)
                .addQueryParameter("from", identifierId)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ResponseEntity<PagedResponse<PrescriptionOutOfStockResponse>> advancedSearch(
            String identifier,
            String phoneNumber,
            String patientName,
            String idNumber,
            LocalDate prescriptionDateStart,
            LocalDate prescriptionDateEnd,
            Pageable pageable
    ) {
        Page<PrescriptionoutOfStock> prescriptionPage = prescriptionOutOfStockRepository.advancedSearch(
                identifier, phoneNumber, patientName, idNumber, prescriptionDateStart, prescriptionDateEnd, pageable
        );

        List<PrescriptionOutOfStockResponse> responseList = prescriptionPage.getContent().stream()
                .map(this::mapToPrescriptionOutOfStockResponse)
                .collect(Collectors.toList());

        PagedResponse<PrescriptionOutOfStockResponse> response = new PagedResponse<>(
                prescriptionPage.getTotalPages(),
                responseList
        );

        return ResponseEntity.ok(response);

    }

    @Override
    public List<PrescriptionOutOfStockResponse> getPatientPrescriptions(Long patientId) {
        List<PrescriptionoutOfStock> prescriptions = prescriptionOutOfStockRepository.findByPatientId(patientId);
        return prescriptions.stream()
                .map(this::mapToPrescriptionOutOfStockResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void testLocationGrouping() {

    }

    private PrescriptionOutOfStockResponse mapToPrescriptionOutOfStockResponse(PrescriptionoutOfStock prescription) {
        PrescriptionOutOfStockResponse response = new PrescriptionOutOfStockResponse();
        BeanUtils.copyProperties(prescription, response);

        if (!prescription.getMedications().isEmpty()) {
            response.setDrugName(prescription.getMedications().get(0).getName());
        }

        response.setStatus(Status.PENDING);

        List<MedicineList> medicines = prescription.getMedications().stream()
                .map(this::mapToMedicineList)
                .collect(Collectors.toList());

        response.setMedicineLists(medicines);
        return response;
    }

    private MedicineList mapToMedicineList(Medication med) {
        MedicineList medDto = new MedicineList();
        BeanUtils.copyProperties(med, medDto);
        return medDto;
    }

    private String calculateDateOfBirth(int age, String ageType) {
        LocalDate now = LocalDate.now();
        if ("Years".equalsIgnoreCase(ageType)) {
            return now.minusYears(age).toString();
        } else if ("Months".equalsIgnoreCase(ageType)) {
            return now.minusMonths(age).toString();
        } else {
            // Default to days if ageType is not recognized
            return now.minusDays(age).toString();
        }
    }

    private String generateUniqueIdentifier(PrescriptionoutOfStock prescription) {
        return "OOS-" + prescription.getId() + "-" +
                prescription.getPrescriptionDate().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

}
