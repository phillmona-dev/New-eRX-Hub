package com.medco.eprescription_out_of_stock.Repository.Users;

import com.medco.eprescription_out_of_stock.Entitiy.User.Role;
import com.medco.eprescription_out_of_stock.Entitiy.User.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Integer> {
    void deleteByRole(Role role);
}
