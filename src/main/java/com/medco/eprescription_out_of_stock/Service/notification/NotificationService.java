package com.medco.eprescription_out_of_stock.Service.notification;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Pharmacy;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;

public interface NotificationService {
    void notifyPatient(Prescription prescription, Pharmacy pharmacy);
}
