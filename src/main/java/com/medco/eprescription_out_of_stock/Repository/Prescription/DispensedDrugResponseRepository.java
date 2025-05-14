package com.medco.eprescription_out_of_stock.Repository.Prescription;
import com.medco.eprescription_out_of_stock.Dto.Response.Dispense.DispensedDrugsResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DispensedDrugResponseRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public List<DispensedDrugsResponseDto> dispensedDrugs() {

        Query query = entityManager.createNativeQuery(
                "SELECT " +
                        "pd.drug_id AS drugId, " +
                        "pr.id AS prescriptionId, " +
                        "pd.id AS prescriptionDrugId, " +
                        "pd.pharmacy_uuid AS pharmacyId, " +
                        "pd.dispensed_by_uuid AS dispensedBy, " +
                        "pr.prescription_date AS prescriptionDate, " +
                        "pr.prescriptions_status AS prescriptionsStatus, " +
                        "pd.drug_instruction AS drugInstruction, " +
                        "pd.drug_dose AS dosage " +
                        "FROM prescriptions AS pr " +
                        "INNER JOIN prescribed_drugs AS pd " +
                        "ON pr.id = pd.prescription_id"
        );
        query.unwrap(NativeQuery.class)
                .setResultTransformer(new AliasToBeanResultTransformer<>(DispensedDrugsResponseDto.class));

        List<DispensedDrugsResponseDto> list = query.getResultList();

        return list;


// Retrieve results as a list of Object arrays
//        List<Object[]> results = query.getResultList();
//
//// Print the results
//        for (Object[] row : results) {
//            Long druId = (Long) row[0];
//            Long prescriptionId = (Long) row[1];
//            Long prescriptionDrugId = (Long) row[2];
//
//
//            System.out.println("Drug ID: " + druId);
//            System.out.println("Prescription ID: " + prescriptionId);
//            System.out.println("Prescription Drug ID: " + prescriptionDrugId);
//
//            System.out.println("---------------------------");
//        }

 }





    public List<DispensedDrugsResponseDto> dispensedDrugsByPharmacyId(Long id) {


        Query query = entityManager.createNativeQuery(
                "SELECT " +
                        "pd.drug_id AS drugId, " +
                        "pr.id AS prescriptionId, " +
                        "pd.id AS prescriptionDrugId, " +
                        "pd.pharmacy_uuid AS pharmacyId, " +
                        "pd.dispensed_by_uuid AS dispensedBy, " +
                        "pr.prescription_date AS prescriptionDate, " +
                        "pr.prescriptions_status AS prescriptionsStatus, " +
                        "pd.drug_instruction AS drugInstruction, " +
                        "pd.drug_dose AS dosage " +
                        "FROM prescriptions AS pr " +
                        "INNER JOIN prescribed_drugs AS pd " +
                        "ON pr.id = pd.prescription_id " +
                        "WHERE pd.pharmacy_uuid = :id"
        );

        query.setParameter("id", id);

        query.unwrap(NativeQuery.class)
                .setResultTransformer(new AliasToBeanResultTransformer<>(DispensedDrugsResponseDto.class));

        List<DispensedDrugsResponseDto> list = query.getResultList();

        return list;



// Retrieve results as a list of Object arrays
//        List<Object[]> results = query.getResultList();
//
//// Print the results
//        for (Object[] row : results) {
//            Long druId = (Long) row[0];
//            Long prescriptionId = (Long) row[1];
//            Long prescriptionDrugId = (Long) row[2];
//
//
//            System.out.println("Drug ID: " + druId);
//            System.out.println("Prescription ID: " + prescriptionId);
//            System.out.println("Prescription Drug ID: " + prescriptionDrugId);
//
//            System.out.println("---------------------------");
//        }

    }



}

