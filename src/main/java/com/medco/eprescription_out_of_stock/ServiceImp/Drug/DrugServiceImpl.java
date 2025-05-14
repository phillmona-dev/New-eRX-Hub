package com.medco.eprescription_out_of_stock.ServiceImp.Drug;

import com.medco.eprescription_out_of_stock.Dto.Request.Drug.DrugDtoRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Drug.DrugFormularyDtoRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.DrugFormulary;
import com.medco.eprescription_out_of_stock.Repository.Drug.DrugRepository;
import com.medco.eprescription_out_of_stock.Service.Drug.DrugService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugServiceImpl  implements DrugService {

    private final DrugRepository drugRepository;

    public DrugServiceImpl(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }


    @Override
    public ResponseEntity<String> addDrug(DrugDtoRequest drugDtoRequest) {

        try {

            Drug drug = new Drug();
            drug.setDrugName(drugDtoRequest.getDrugName());
            drug.setDrugCode(drugDtoRequest.getDrugCode());
            drug.setCategory(drugDtoRequest.getCategory());
            drug.setSubCategory(drugDtoRequest.getSubCategory());
            drug.setStrength(drugDtoRequest.getStrength());
            drug.setVolume(drugDtoRequest.getVolume());
            drug.setDrugUnit(drugDtoRequest.getDrugUnit());
            drug.setDosageForm(drugDtoRequest.getDosageForm());
            drug.setDrugDescription(drugDtoRequest.getDrugDescription());

            DrugFormulary formulary = new DrugFormulary();
            DrugFormularyDtoRequest fDto = drugDtoRequest.getDrugFormularyDtoRequest();
            formulary.setIndications(fDto.getIndications());
            formulary.setCautions(fDto.getCautions());
            formulary.setContraindications(fDto.getContraindications());
            formulary.setDrugInteractions(fDto.getDrugInteractions());
            formulary.setSideEffects(fDto.getSideEffects());
            formulary.setDoseAndAdministration(fDto.getDoseAndAdministration());
            formulary.setStorage(fDto.getStorage());
            formulary.setDrug(drug);
            drug.setDrugFormulary(formulary);
            drugRepository.save(drug);

            return new ResponseEntity<>("Drug added successfully", HttpStatus.CREATED);


        } catch (Exception e) {
            System.out.println("sssssssss" +e.getMessage().toString());
            return new ResponseEntity<>("Error while adding the drug", HttpStatus.INTERNAL_SERVER_ERROR);
        }







    }

    @Override
    public Drug getDrugById(String drugId) {
        return null;
    }

    @Override
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    @Override
    public List<Drug> searchDrugsByName(String name) {
        return drugRepository.findByDrugNameContainingIgnoreCase(name);

    }
}
