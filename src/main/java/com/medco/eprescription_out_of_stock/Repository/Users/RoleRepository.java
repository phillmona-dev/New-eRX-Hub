package com.medco.eprescription_out_of_stock.Repository.Users;
import com.medco.eprescription_out_of_stock.Entitiy.User.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByRoleName(String roleName);

    Boolean existsByRoleDescription(String roleDescription);

    Role findByRoleUuid(String roleUuid);

    Page<Role> findByRoleNameContainingOrRoleDescriptionContaining(String searchTerm1, String searchTerm2,
                                                                   Pageable pageRequest);
    List<Role> findAllByRoleDescription(String roleDescription);
    List<Role> findAllByRoleName(String roleName);

    String findRoleNameById(Integer roleId);

    Role findByRoleName(String insured);
}