package com.medco.eprescription_out_of_stock.Repository.Users;

import com.medco.eprescription_out_of_stock.Entitiy.User.User;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

    List<User> findByRoleName(String roleName);
    Boolean existsByEmail(String email);
    User findByEmail(String email);
    User findByUserUuid(String userUuid);
    boolean existsByMobilePhone(String mobilePhone);
    User findByEmailAndPasswordResetCode(String email, String passwordResetCode);
    User findByEmailVerificationToken(String emailVerificationToken);
    Page<User> findByFirstNameContainingAndFatherNameContaining(String firstName, String fatherName, Pageable pageableRequest);
    Page<User> findByFirstNameContainingAndFatherNameContainingAndGrandFatherNameContaining(String firstName, String fatherName, String grandFatherName, Pageable pageableRequest);
    Page<User> findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrMobilePhoneContainingOrEmailContaining(String searchKey, String searchKey1, String searchKey2, String searchKey3, String searchKey4,  Pageable pageableRequest);
    List<User> findByRoleNameAndUserStatus(String insured,  Status userStatus);
    Page<User> findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrMobilePhoneContainingOrEmailContainingAndUserStatus(String searchKey, String searchKey1, String searchKey2, String searchKey3, String searchKey4, Status status, Pageable pageableRequest);

}
