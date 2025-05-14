package com.medco.eprescription_out_of_stock.Service.Pharmacy;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.pharmacy.PharmacyRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Pharmacy.PharmacyResponseDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PharmacyService {
    ResponseEntity<Pharmacy> createPharmacy(PharmacyRequestDto req);

    PharmacyResponseDto getAllPharmacy(String searchKey, Pageable pageable);
    ResponseEntity<Pharmacy> updatePharmacy(Long id, PharmacyRequestDto req);
    ResponseEntity<Pharmacy> getPharmacyById(Long id);


    Boolean delete(Long id);
}
