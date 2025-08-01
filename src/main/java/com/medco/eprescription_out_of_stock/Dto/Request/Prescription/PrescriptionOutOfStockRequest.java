package com.medco.eprescription_out_of_stock.Dto.Request.Prescription;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrescriptionOutOfStockRequest {

   private PetientDetail patientDetail;
    private List<MedicineList> medicineLists;

}
