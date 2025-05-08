package com.medco.eprescription_out_of_stock.ServiceImp.User;

import com.medco.eprescription_out_of_stock.Repository.Users.PrivilegeRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.RolePrivilegeRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.RoleRepository;
import com.medco.eprescription_out_of_stock.Dto.Request.User.AddRolePrivilegesRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.RoleRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.RoleUpdateRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.MessageResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.RoleMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.RoleResponse;
import com.medco.eprescription_out_of_stock.Entitiy.User.Privilege;
import com.medco.eprescription_out_of_stock.Entitiy.User.Role;
import com.medco.eprescription_out_of_stock.Entitiy.User.RolePrivilege;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Service.User.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final RolePrivilegeRepository rolePrivilegeRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, RolePrivilegeRepository rolePrivilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }


    @Override
    public RoleResponse createRole(RoleRequest roleRequest) throws BadRequestException {
        if (roleRepository.existsByRoleName(roleRequest.getRoleName())) {
            throw new BadRequestException("Error: Role Name is already registered!");
        }
        if (roleRepository.existsByRoleDescription(roleRequest.getRoleDescription())) {
            throw new BadRequestException("Error: Role description is already registered!");
        }

        Role role = new Role(roleRequest.getRoleName(), roleRequest.getRoleDescription());
        Set<RolePrivilege> rolePrivileges = new HashSet<>();
        List<String> privilegeUuids = new ArrayList<>();

        if (roleRequest.getPrivileges() != null && roleRequest.getPrivileges().length > 0) {
            for (String privilegeName : roleRequest.getPrivileges()) {
                try {
                    System.out.println("Checking privilege: " + privilegeName);
                    Privilege privilege = privilegeRepository.findByPrivilegeName(privilegeName);

                    if (privilege == null) {
                        System.err.println("Error: Privilege with name " + privilegeName + " does not exist!");
                        throw new BadRequestException("Error: Privilege with name " + privilegeName + " does not exist!");
                    }

                    RolePrivilege rolePrivilege = new RolePrivilege();
                    rolePrivilege.setRole(role);
                    rolePrivilege.setPrivilege(privilege);
                    rolePrivileges.add(rolePrivilege);

                    privilegeUuids.add(privilege.getPrivilegeUuid());
                } catch (Exception e) {
                    System.err.println("Exception while checking privilege: " + privilegeName);
                    e.printStackTrace();
                    throw new BadRequestException("Unexpected error while processing privilege: " + privilegeName);
                }
            }
        }


        roleRepository.save(role);

        for (RolePrivilege rolePrivilege : rolePrivileges) {
            rolePrivilegeRepository.save(rolePrivilege);
        }
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRoleName(role.getRoleName());
        roleResponse.setRoleDescription(role.getRoleDescription());
        roleResponse.setRoleUuid(role.getRoleUuid());
        roleResponse.setPrivileges(privilegeUuids);

        return roleResponse;
    }



    @Override
    public ResponseEntity<?> updateRole(String roleUuid, RoleUpdateRequest roleUpdateRequest) {


        Role role = roleRepository.findByRoleUuid(roleUuid);

        if (role == null) {
            throw new RuntimeException("Error: Role is not found in the database.");
        }

        List<Role> roleName = roleRepository.findAllByRoleName(roleUpdateRequest.getRoleName());
        List<Role> roleDesc = roleRepository.findAllByRoleDescription(roleUpdateRequest.getRoleDescription());
        if(role == null)
            new RuntimeException("Error: role is not found in database.");
        if (roleName.size() >=2  ||  roleDesc.size() >=2)
            throw new RuntimeException("Role by the role name or role description is exist. Update a role to have unique name and description");


        role.setRoleName(roleUpdateRequest.getRoleName());
        role.setRoleDescription(roleUpdateRequest.getRoleDescription());
        Set<Privilege> existingPrivileges = new HashSet<>(role.getPrivileges());
        Set<String> newPrivilegeNames = new HashSet<>(Arrays.asList(roleUpdateRequest.getPrivileges()));
        existingPrivileges.removeIf(privilege -> !newPrivilegeNames.contains(privilege.getPrivilegeName()));
        for (String privilegeName : newPrivilegeNames) {
            Privilege rolePrivilege = privilegeRepository.findByPrivilegeName(privilegeName);
            if (rolePrivilege == null) {
                throw new RuntimeException("Error: Privilege '" + privilegeName + "' is not found in the database.");
            }
            existingPrivileges.add(rolePrivilege);
        }

        role.setPrivileges(existingPrivileges);

        roleRepository.save(role);


        return ResponseEntity.ok(new MessageResponse("Role updated successfully!"));

    }


    @Override
    public ResponseEntity<?> deleteRole(String roleUuid) {

        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("Role not found.");
        roleRepository.delete(role);
        return ResponseEntity.ok(new MessageResponse("Role soft deleted permanently!"));

    }

    @Override
    public RoleResponse getRole(String roleUuid) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("roleUuid not found.");

        RoleResponse response = new RoleResponse();
        List<String> rolePrivileges = privilegeRepository.findPrivilegeNamesByRoleUuid(roleUuid);

        BeanUtils.copyProperties(role, response);
        response.setPrivileges(rolePrivileges);

        return response;
    }


    @Override
    public RoleMyResponse getRoles(int page, int limit) {
        if (page > 0) page = page - 1;

        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Role> rolePage = roleRepository.findAll(pageRequest);
        long totalPages = rolePage.getTotalPages();
        List<Role> roleList = rolePage.getContent();

        RoleMyResponse response = new RoleMyResponse();
        response.setTotalPages(totalPages);

        List<RoleResponse> roleResponseList = new ArrayList<>();
        for (Role role : roleList) {
            RoleResponse roleResponse = new RoleResponse();
            BeanUtils.copyProperties(role, roleResponse);

            // Fetch associated privilege names instead of UUIDs
            List<String> privilegeNames = role.getPrivileges().stream()
                    .map(privilege -> privilegeRepository.findPrivilegeNameByUuid(privilege.getPrivilegeUuid()))
                    .collect(Collectors.toList());

            roleResponse.setPrivileges(privilegeNames);
            roleResponseList.add(roleResponse);
        }

        response.setResponse(roleResponseList);
        return response;
    }




    @Override
    public ResponseEntity<?> addRolePrivileges(String roleUuid, AddRolePrivilegesRequest rolePrivilegesRequest) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("Role not found.");
        String[] privilegeNames = rolePrivilegesRequest.getPrivileges();
        Set<Privilege> privileges = role.getPrivileges();

        for (String privilegeName : privilegeNames) {
            //System.out.println("-------------------"+privilegeName);
            Privilege rolePrivilege = privilegeRepository.findByPrivilegeName(privilegeName);
            if (rolePrivilege == null) {
                throw new RuntimeException("Error: Privilege is not found in database.");
            }
            privileges.add(rolePrivilege);
        }

        role.setPrivileges(privileges);
        roleRepository.save(role);
        return ResponseEntity.ok(new MessageResponse("Privileges added to Role successfully!"));
    }

    @Override
    public ResponseEntity<?> deleteRolePrivileges(String roleUuid, AddRolePrivilegesRequest rolePrivilegesRequest) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("Role not found.");

        String[] privilegeNames = rolePrivilegesRequest.getPrivileges();
        Set<Privilege> privileges = role.getPrivileges();
        Set<Privilege> deletedPrivileges = new HashSet<>();
        Set<Privilege> filteredPrivileges = new HashSet<>(privileges);

        for (String privilegeName : privilegeNames) {
            Privilege rolePrivilege = privilegeRepository.findByPrivilegeName(privilegeName);
            if (rolePrivilege == null) {
                throw new RuntimeException("Error: Privilege is not found in database.");
            }
            deletedPrivileges.add(rolePrivilege);
        }

        filteredPrivileges.removeAll(deletedPrivileges);
        role.setPrivileges(filteredPrivileges);
        roleRepository.save(role);
        return ResponseEntity.ok(new MessageResponse("Privileges deleted from a Role successfully!"));
    }

    @Override
    public List<RoleResponse> searchRoles(String searchTerm, int page, int limit) {
        if (page > 0) page = page - 1;

        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());

        Page<Role> rolesPage = roleRepository.findByRoleNameContainingOrRoleDescriptionContaining(searchTerm, searchTerm, pageRequest);


        int totalPages = rolesPage.getTotalPages();
        List<Role> roleList = rolesPage.getContent();
        List<RoleResponse> roleResponse = new ArrayList<>();
        for (Role r : roleList) {
            RoleResponse rr = new RoleResponse();
            if (roleResponse.size() == 0)
                // rr.setTotalPages(totalPages);
                BeanUtils.copyProperties(r, rr);
            roleResponse.add(rr);
        }

        return roleResponse;
    }
}
