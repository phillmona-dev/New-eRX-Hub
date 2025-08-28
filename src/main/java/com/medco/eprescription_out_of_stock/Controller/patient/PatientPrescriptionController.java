package com.medco.eprescription_out_of_stock.Controller.patient;

import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Prescription;
import com.medco.eprescription_out_of_stock.Repository.Prescription.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/api/v1/erx/patient")
public class PatientPrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @GetMapping("/{patientId}/prescriptions")
    public String viewPrescriptions(@PathVariable Long patientId, Model model) {
        List<Prescription> prescriptions = (List<Prescription>) prescriptionRepository.findByPatientId(patientId, Pageable.unpaged());
        model.addAttribute("prescriptions", prescriptions);
        return "patient/prescriptions";
    }
}
