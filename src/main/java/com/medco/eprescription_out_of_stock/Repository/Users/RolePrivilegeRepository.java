package com.medco.eprescription_out_of_stock.Repository.Users;

import com.medco.eprescription_out_of_stock.Entitiy.User.Role;
import com.medco.eprescription_out_of_stock.Entitiy.User.RolePrivilege;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Integer> {
//    void deleteByRole(Role role);

    @Transactional
    @Modifying
    @Query("DELETE FROM RolePrivilege rp WHERE rp.role = :role")
    void deleteByRole(@Param("role") Role role);
}
