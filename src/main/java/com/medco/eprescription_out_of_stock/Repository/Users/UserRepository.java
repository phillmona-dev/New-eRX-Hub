package com.medco.eprescription_out_of_stock.Repository.Users;
import com.medco.eprescription_out_of_stock.Entitiy.User.User;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    User findByEmail(String email);

//    Optional<User> findByEmail(String email);
    User findByUserUuid(String userUuid);
    void deleteByUserUuid(String userUuid);
    boolean existsByMobilePhone(String mobilePhone);

    User findByEmailVerificationToken(String emailVerificationToken);

    Page<User> findByFirstNameContainingAndFatherNameContaining(String firstName, String fatherName, Pageable pageableRequest);

    Page<User> findByFirstNameContainingAndFatherNameContainingAndGrandFatherNameContaining(String firstName, String fatherName, String grandFatherName, Pageable pageableRequest);

    User findByEmailAndPasswordResetCode(String email, String passwordResetCode);

    Optional<User> findByEmailOrMobilePhone(String email, String mobilePhone);

    Page<User> findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrMobilePhoneContainingOrEmailContaining(String searchKey, String searchKey1, String searchKey2, String searchKey3, String searchKey4, Pageable pageableRequest);

    Page<User> findByUserStatus(UserStatus status, Pageable pageableRequest);

    Page<User> findByUserType(UserType userType, Pageable pageableRequest);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.pharmacistDetail WHERE u.userUuid = :userUuid")
    User findByUserUuidWithDetails(@Param("userUuid") String userUuid);

    @Override
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.pharmacistDetail")
    Page<User> findAll(Pageable pageable);

    User findByRoleUuid(String roleUuid);

}
