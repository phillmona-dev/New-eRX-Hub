package com.medco.eprescription_out_of_stock.ServiceImp.Prescription;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.MedicineList;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.PrescriptionOutOfStockRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.Prescription.PrescriptionOutOfStockResponse;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Medication;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.PrescriptionoutOfStock;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionOutOfStockRepository;
import com.medco.eprescription_out_of_stock.Service.Prescription.PrescriptionOutOfStockService;
import com.medco.eprescription_out_of_stock.Utills.PagedResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionOutOfStockServiceImpl implements PrescriptionOutOfStockService {

    private final PrescriptionOutOfStockRepository prescriptionOutOfStockRepository;

    public PrescriptionOutOfStockServiceImpl(PrescriptionOutOfStockRepository prescriptionOutOfStockRepository) {
        this.prescriptionOutOfStockRepository = prescriptionOutOfStockRepository;
    }

    @Override
    public PrescriptionOutOfStockRequest createRequest(PrescriptionOutOfStockRequest request) {

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
        return request;

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
