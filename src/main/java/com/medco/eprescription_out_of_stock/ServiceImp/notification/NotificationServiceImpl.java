package com.medco.eprescription_out_of_stock.ServiceImp.notification;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;
import com.medco.eprescription_out_of_stock.Service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private AfroMessageService afroMessageService;

    @Override
    public void notifyPatient(Prescription prescription, Pharmacy pharmacy) {
        String message = String.format(
                "Your prescription for %s is now available at %s. Address: %s",
                prescription.getDrugName(),
                pharmacy.getPharmacyName(),
                pharmacy.getWoredaId()
        );

        try {
            afroMessageService.sendSms(prescription.getPatientContact(), message);
        } catch (IOException e) {
            // Log the error and possibly implement a retry mechanism
            e.printStackTrace();
        }
    }
}
