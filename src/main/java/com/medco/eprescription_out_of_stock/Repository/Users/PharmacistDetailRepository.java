package com.medco.eprescription_out_of_stock.Repository.Users;

import com.medco.eprescription_out_of_stock.Entitiy.User.UserPharmacistDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacistDetailRepository extends JpaRepository <UserPharmacistDetail,Long> {

    Page<UserPharmacistDetail> findByUser_FirstNameContainingOrUser_FatherNameContainingOrUser_GrandFatherNameContainingOrUser_MobilePhoneContainingOrUser_EmailContaining(String searchKey, String searchKey1, String searchKey2, String searchKey3, String searchKey4, Pageable pageableRequest);

    Page<UserPharmacistDetail> findByUser_FirstNameContainingOrUserFatherNameContaining(String firstName, String fatherName, Pageable pageableRequest);

    Page<UserPharmacistDetail> findByUser_FirstNameContainingOrUser_FatherNameContainingOrUser_GrandFatherNameContaining(String firstName, String fatherName, String grandFatherName, Pageable pageableRequest);
}
