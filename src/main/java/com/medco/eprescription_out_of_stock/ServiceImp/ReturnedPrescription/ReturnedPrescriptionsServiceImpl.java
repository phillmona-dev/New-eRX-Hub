package com.medco.eprescription_out_of_stock.ServiceImp.ReturnedPrescription;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Prescription.ReturnedPrescriptionRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.ReturnedPrescriptions;
import com.medco.eprescription_out_of_stock.Repository.Prescription.ReturnedPrescriptionsRepository;
import com.medco.eprescription_out_of_stock.Service.ReturnedPrescription.ReturnedPrescriptionsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReturnedPrescriptionsServiceImpl implements ReturnedPrescriptionsService {

    @Autowired
    private ReturnedPrescriptionsRepository returnedPrescriptionsRepository;

    @Override
    public ResponseEntity<?> createReturnedPrescription(ReturnedPrescriptionRequest request) {

        ReturnedPrescriptions rp=new ReturnedPrescriptions();
        BeanUtils.copyProperties(request,rp);
        rp.setReturnedPrescriptionUuid(UUID.randomUUID().toString());

        ReturnedPrescriptions savedRp= returnedPrescriptionsRepository.save(rp);

        return  ResponseEntity.ok(savedRp);


    }

    @Override
    public ReturnedPrescriptions getReturnedPrescriptionById(Long id) {
        Optional<ReturnedPrescriptions> optionalReturnedPrescription = returnedPrescriptionsRepository.findById(String.valueOf(id));
        return optionalReturnedPrescription.orElse(null);
    }

    @Override
    public List<ReturnedPrescriptions> getAllReturnedPrescriptions() {
        return returnedPrescriptionsRepository.findAll();
    }

    @Override
    public ReturnedPrescriptions updateReturnedPrescription(Long id, ReturnedPrescriptions updatedReturnedPrescription) {
        Optional<ReturnedPrescriptions> optionalReturnedPrescription = returnedPrescriptionsRepository.findById(String.valueOf(id));
        if (optionalReturnedPrescription.isPresent()) {
            ReturnedPrescriptions existingReturnedPrescription = optionalReturnedPrescription.get();
            existingReturnedPrescription.setComment(updatedReturnedPrescription.getComment());
            existingReturnedPrescription.setReturnedDate(updatedReturnedPrescription.getReturnedDate());
            existingReturnedPrescription.setDeleted(updatedReturnedPrescription.isDeleted());
            return returnedPrescriptionsRepository.save(existingReturnedPrescription);
        }
        return null;
    }

    @Override
    public void deleteReturnedPrescription(Long id) {
        returnedPrescriptionsRepository.deleteById(String.valueOf(id));
    }
}
