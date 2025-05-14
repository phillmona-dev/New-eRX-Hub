package com.medco.eprescription_out_of_stock.ServiceImp.Pharmacy;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.pharmacy.PharmacyRequestDto;
import com.medco.eprescription_out_of_stock.Dto.Response.Pharmacy.PharmacyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.Pharmacy.PharmacyResponseDto;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PharmacyRepository;
import com.medco.eprescription_out_of_stock.Service.Pharmacy.PharmacyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PharmacyServiceImp  implements PharmacyService {

    @Autowired
    PharmacyRepository pharmacyRepository;

    @Override
    public ResponseEntity<Pharmacy> createPharmacy(PharmacyRequestDto req) {

        Pharmacy pharmacy=new Pharmacy();
        BeanUtils.copyProperties(req,pharmacy);
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        return ResponseEntity.ok(savedPharmacy);

    }

    @Override
    public PharmacyResponseDto getAllPharmacy(String searchKey, Pageable pageable) {

        return (searchKey != null && !searchKey.isEmpty())
                ? getPharmacyWithSearch(searchKey, pageable)
                : getPharmacyWithoutSearch(pageable);


    }


    private PharmacyResponseDto getPharmacyWithSearch(String searchKey, Pageable pageable) {

        Page<Pharmacy> pharmacyWithPage = pharmacyRepository.findByPharmacyNameContaining(searchKey, pageable);
        PharmacyResponseDto pharmacyResponseDto = new PharmacyResponseDto();
        pharmacyResponseDto.setTotalPage((long) pharmacyWithPage.getTotalPages());

        List<PharmacyResponse> responseList = pharmacyWithPage.getContent().stream()
                .map(pharmacy -> {
                    PharmacyResponse ph = new PharmacyResponse();

                    BeanUtils.copyProperties(pharmacy, ph);

                    return ph;
                })
                .collect(Collectors.toList());

        pharmacyResponseDto.setResponse(responseList);
        return  pharmacyResponseDto;



    }


    private PharmacyResponseDto getPharmacyWithoutSearch(Pageable pageable) {

        Page<Pharmacy> pharmacyWithPage = pharmacyRepository.findAll(pageable);
        PharmacyResponseDto pharmacyResponseDto = new PharmacyResponseDto();
        pharmacyResponseDto.setTotalPage((long) pharmacyWithPage.getTotalPages());

        List<PharmacyResponse> responseList = pharmacyWithPage.getContent().stream()
                .map(pharmacy -> {
                    PharmacyResponse ph = new PharmacyResponse();

                    BeanUtils.copyProperties(pharmacy, ph);

                    return ph;
                })
                .collect(Collectors.toList());

        pharmacyResponseDto.setResponse(responseList);
        return  pharmacyResponseDto;



    }

    @Override
    public ResponseEntity<Pharmacy> updatePharmacy(Long id, PharmacyRequestDto req) {
        Optional<Pharmacy> existingPharmacy = pharmacyRepository.findById(id);
        if (existingPharmacy.isPresent()) {
            Pharmacy pharmacyToUpdate = existingPharmacy.get();
            BeanUtils.copyProperties(req, pharmacyToUpdate);

            Pharmacy updatedPharmacy = pharmacyRepository.save(pharmacyToUpdate);
            return ResponseEntity.ok(updatedPharmacy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Pharmacy> getPharmacyById(Long id) {

        Optional<Pharmacy> pharmacy = pharmacyRepository.findById(id);
        if (pharmacy.isPresent()) {
            return ResponseEntity.ok(pharmacy.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public Boolean delete(Long id) {

        Optional<Pharmacy> pharmacy = pharmacyRepository.findById(id);

        if (pharmacy.isPresent()) {
            try {
                pharmacyRepository.delete(pharmacy.get());
                return true;
            } catch (Exception e) {
                System.err.println("Error while deleting pharmacy: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }


    }

}
