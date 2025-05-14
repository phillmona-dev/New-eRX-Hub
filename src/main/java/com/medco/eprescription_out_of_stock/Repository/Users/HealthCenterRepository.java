package com.medco.eprescription_out_of_stock.Repository.Users;

import com.medco.eprescription_out_of_stock.Entitiy.User.HealthCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HealthCenterRepository extends JpaRepository<HealthCenter, Long> {

    boolean existsByEmail(String email);

    boolean existsByMobilePhone(String mobilePhone);

    Optional<HealthCenter> findByEmail(String email);
    Optional<HealthCenter> findByMobilePhone(String mobilePhone);
    List<HealthCenter> findByNameContainingIgnoreCase(String name);

    // Custom query for searching with pagination support
    @Query("SELECT h FROM HealthCenter h WHERE " +
            "LOWER(h.email) = LOWER(:identifier) OR " +
            "LOWER(h.mobilePhone) = LOWER(:identifier) OR " +
            "LOWER(h.name) LIKE LOWER(CONCAT('%', :identifier, '%'))")
    Page<HealthCenter> searchByIdentifier(@Param("identifier") String identifier, Pageable pageable);

}
