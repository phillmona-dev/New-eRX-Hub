package com.medco.eprescription_out_of_stock.Repository.Drug;

import com.medco.eprescription_out_of_stock.Entitiy.Drug.Drug;
import com.medco.eprescription_out_of_stock.Entitiy.Drug.DrugFormulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugFormularyRepository  extends JpaRepository<DrugFormulary,Long> {
}
