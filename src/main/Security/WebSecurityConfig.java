package com.lemobile.lemobile.Security;
import com.lemobile.lemobile.Security.Jwt.AuthEntryPointJwt;
import com.lemobile.lemobile.Security.Jwt.AuthTokenFilter;
import com.lemobile.lemobile.Security.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(

                                "/api/v1/lemobile/auth/users/sign-in",
                                "/api/v1/lemobile/auth/users/sign-up",
                                "/api/v1/lemobile/auth/insured-users/**",
                                "/api/v1/lemobile/users/role/**",
                                "api/v1/lemobile/auth/insured-users/suspended-users",
                                "api/v1/lemobile/auth/insured-users/open",
                                 "api/v1/lemobile/claims/*",
                                 "api/v1/lemobile/claims/get-claim-by-id/{id}",
                                  "api/v1/lemobile/claims/get-claim-detail/{id}",
                                  "/api/v1/lemobile/premium/*",
                                  "/api/v1/premium/lemobile/amount",
                                  "api/v1/lemobile/auth/insured-users/register-device",
                                 "/api/v1/lemobile/auth/insured-users/customer-sign-up",
                                "api/v1/lemobile/auth/insured-users",
                                "/api/v1/lemobile/auth/users/sign-up",
                                "/api/v1/lemobile/auth/insured-users/get-policies",
                                "/api/v1/lemobile/auth/insured-users/get-policies/{id}",
                                "api/v1/premium",
                                "api/v1/lemobile/claims/get-claim-by-user-id/{id}",
                                "/api/v1/lemobile/users/role/**",
                                "/api/v1/lemobile/claims/update-claim/{id}",
                                "/api/v1/lemobile/users/privilege/**",
                                "/api/v1/lemobile/auth/insured-users/{id}",
                                "/api/v1/lemobile/payment/**",
                                "/api/v1/lemobile/auth/opt-message/send-otp",
                                "/api/v1/lemobile/auth/opt-message/verify-otp",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/api/v1/lmobile/users/privilege/**",
                                "/actuator/**",
                                "/health",
                                "/info",
                                "/beans",
                                "/admin",
                                "/metrics/jvm.memory.use"


                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
