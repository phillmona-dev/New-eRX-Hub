package com.medco.eprescription_out_of_stock.shared;

import com.medco.eprescription_out_of_stock.Security.Services.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Optional.ofNullable(userPrincipal.getUserUuid());
    }
}
