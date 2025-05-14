package com.medco.eprescription_out_of_stock.Repository.Users;
import com.medco.eprescription_out_of_stock.Entitiy.User.UserPatientDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientDetailRepository extends JpaRepository<UserPatientDetail, Long> {

//    boolean existsByEmail(String email);

//    boolean existsByMobilePhone(String MobilePhone);

    Optional<UserPatientDetail> findByUserId(Long userId);

    Page<UserPatientDetail> findByUser_FirstNameContainingOrUser_FatherNameContainingOrUser_GrandFatherNameContainingOrUser_MobilePhoneContainingOrUser_EmailContainingOrMRNContainingOrMPNContaining(String searchKey, String searchKey1, String searchKey2, String searchKey3, String searchKey4, String searchKey5, String searchKey6, Pageable pageableRequest);

    Page<UserPatientDetail> findByUser_FirstNameContainingAndUser_FatherNameContaining(String firstName, String fatherName, Pageable pageableRequest);

    Page<UserPatientDetail> findByUser_FirstNameContainingAndUser_FatherNameContainingAndUser_GrandFatherNameContaining(String firstName, String fatherName, String grandFatherName, Pageable pageableRequest);
}
