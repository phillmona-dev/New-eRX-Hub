package com.medco.eprescription_out_of_stock.Security.Services;
import com.medco.eprescription_out_of_stock.Entitiy.User.User;
import com.medco.eprescription_out_of_stock.Exception.EmailAlreadyExists;
import com.medco.eprescription_out_of_stock.Repository.Users.AuthRepository;
import com.medco.eprescription_out_of_stock.Repository.Users.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AuthRepository userRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        User user = userRepository.findByEmail(email);
        if (user==null) throw new EmailAlreadyExists("User Not Found with email: " + email);

        List<String> privilegesForRole = privilegeRepository.findPrivilegeNamesByRoleUuid(String.valueOf(user.getRoleUuid()));

        privilegesForRole = privilegesForRole.stream().map(p -> "ROLE_" + p).collect(Collectors.toList());
        return UserDetailsImpl.build(user,privilegesForRole);
    }

}
