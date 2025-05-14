package com.medco.eprescription_out_of_stock.ServiceImp.Prescription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medco.eprescription_out_of_stock.Dto.OptMessage.OtpResponse;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.MedicineList;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Medication;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescriptionoutOfStock;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionOutOfStockRepository;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionOutOfStockService;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import okhttp3.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PrescriptionOutOfStockServiceImpl implements PrescriptionOutOfStockService {


    @Value("${afromessage.api.url}")
    private String baseURL;
    @Value("${afromessage.api.token}")
    private String token;
    @Value("${afromessage.api.identifierId}")
    private String identifierId;

    @Value("${kenema.api.url}")
    private String kenemaUrl;

    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();


    private final PrescriptionOutOfStockRepository prescriptionOutOfStockRepository;

    public PrescriptionOutOfStockServiceImpl(PrescriptionOutOfStockRepository prescriptionOutOfStockRepository) {
        this.prescriptionOutOfStockRepository = prescriptionOutOfStockRepository;
    }

    @Override
    public ResponseEntity<OtpResponse> createRequest(PrescriptionOutOfStockRequest request) {

        List<String> medicineNames = request.getMedicineLists()
                .stream()
                .map(med -> med.getName())
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


}
