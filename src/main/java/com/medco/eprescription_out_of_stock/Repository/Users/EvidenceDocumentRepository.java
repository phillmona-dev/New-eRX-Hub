package com.medco.eprescription_out_of_stock.Repository.Users;
import com.medco.eprescription_out_of_stock.Entitiy.User.EvidenceDocument;
import com.medco.eprescription_out_of_stock.Entitiy.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceDocumentRepository extends JpaRepository<EvidenceDocument,Long> {

    List<EvidenceDocument> findByUser(User user);

}
