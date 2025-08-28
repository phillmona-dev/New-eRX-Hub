package com.medco.eprescription_out_of_stock.config;

import com.medco.eprescription_out_of_stock.Entitiy.User.Privilege;
import com.medco.eprescription_out_of_stock.Entitiy.User.Role;
import com.medco.eprescription_out_of_stock.Entitiy.User.RolePrivilege;
import com.medco.eprescription_out_of_stock.Entitiy.User.User;
import com.medco.eprescription_out_of_stock.Repository.Users.PrivilegeRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.RolePrivilegeRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.RoleRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.UserRepository;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import com.medco.eprescription_out_of_stock.shared.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String SUPER_ADMIN_EMAIL = "medco@gmail.com";

    @Override
    @Transactional
    public void run(String... args) {
        loadPrivileges();
        loadRoles();
        loadUsers();
    }

    private void loadPrivileges() {
        List<String> privilegeNames = Arrays.asList(
                "CREATE_USER", "READ_USER", "UPDATE_USER", "DELETE_USER",
                "CREATE_ROLE", "READ_ROLE", "UPDATE_ROLE", "DELETE_ROLE",
                "CREATE_PRIVILEGE", "READ_PRIVILEGE", "UPDATE_PRIVILEGE", "DELETE_PRIVILEGE", "VIEW_USER",
                "Delete Drugs", "Update Drugs", "Create Drugs",
                "Delete Services", "Update Services", "Create Services", "CREATE_SERVICE"
        );

        for (String name : privilegeNames) {
            if (privilegeRepository.findByPrivilegeName(name) == null) {
                Privilege privilege = new Privilege();
                privilege.setPrivilegeName(name);
                privilege.setPrivilegeDescription("Allows " + name.toLowerCase().replace("_", " "));
                privilege.setPrivilegeCategory("SYSTEM");
                privilege.setPrivilegeType("FOR_SYSTEM_ADMIN");
                privilegeRepository.save(privilege);
                System.out.println("Created privilege: " + name);
            }
        }
    }

    @Transactional
    private void loadRoles() {
        Role superAdminRole = roleRepository.findByRoleName("ROLE_SUPER_ADMIN");

        if (superAdminRole == null) {
            superAdminRole = new Role();
            superAdminRole.setRoleName("ROLE_SUPER_ADMIN");
            superAdminRole.setRoleDescription("Super Administrator Role");
            roleRepository.save(superAdminRole);
            System.out.println("Created ROLE_SUPER_ADMIN");
        }

        // Privileges required for super admin
        List<String> superAdminPrivilegeNames = Arrays.asList(
                "CREATE_USER", "READ_USER", "UPDATE_USER", "DELETE_USER",
                "CREATE_ROLE", "READ_ROLE", "UPDATE_ROLE", "DELETE_ROLE",
                "CREATE_PRIVILEGE", "READ_PRIVILEGE", "UPDATE_PRIVILEGE", "DELETE_PRIVILEGE", "VIEW_USER"
        );

        for (String privilegeName : superAdminPrivilegeNames) {
            Privilege privilege = privilegeRepository.findByPrivilegeName(privilegeName);
            if (privilege != null) {
                // Only create RolePrivilege if it doesn’t exist
                boolean exists = rolePrivilegeRepository.existsByRoleAndPrivilege(superAdminRole, privilege);
                if (!exists) {
                    RolePrivilege rolePrivilege = new RolePrivilege();
                    rolePrivilege.setRole(superAdminRole);
                    rolePrivilege.setPrivilege(privilege);
                    rolePrivilegeRepository.save(rolePrivilege);
                    System.out.println("Linked " + privilegeName + " to ROLE_SUPER_ADMIN");
                }
            }
        }
    }

    private void loadUsers() {
        User existingSuperAdmin = userRepository.findByEmail(SUPER_ADMIN_EMAIL);

        Role superAdminRole = roleRepository.findByRoleName("ROLE_SUPER_ADMIN");
        if (superAdminRole == null) {
            throw new RuntimeException("ROLE_SUPER_ADMIN not found. Ensure roles are loaded before users.");
        }

        if (existingSuperAdmin == null) {
            User superAdmin = new User();
            superAdmin.setEmail(SUPER_ADMIN_EMAIL);
            superAdmin.setPassword(passwordEncoder.encode("passme"));
            superAdmin.setTitle("Mr.");
            superAdmin.setFirstName("Super");
            superAdmin.setFatherName("Admin");
            superAdmin.setGrandFatherName("System");
            superAdmin.setGender("male");
            superAdmin.setMobilePhone("1234567890");
            superAdmin.setStatus(Status.ACTIVE);
            superAdmin.setUserStatus(UserStatus.ACTIVE);
            superAdmin.setUserType(UserType.ADMIN);

            superAdmin.setRoleName(superAdminRole.getRoleName());
            superAdmin.setRoleUuid(superAdminRole.getRoleUuid());
            superAdmin.getRoles().add(superAdminRole);
            superAdminRole.setUser(superAdmin);

            userRepository.save(superAdmin);
            System.out.println("Super Admin user created successfully.");
        } else {
            // Ensure role info is up to date
            existingSuperAdmin.setRoleName(superAdminRole.getRoleName());
            existingSuperAdmin.setRoleUuid(superAdminRole.getRoleUuid());

            if (!existingSuperAdmin.getRoles().contains(superAdminRole)) {
                existingSuperAdmin.getRoles().add(superAdminRole);
            }
            superAdminRole.setUser(existingSuperAdmin);

            userRepository.save(existingSuperAdmin);
            System.out.println("Super Admin user already exists. Updated role info if necessary.");
        }
    }
}
