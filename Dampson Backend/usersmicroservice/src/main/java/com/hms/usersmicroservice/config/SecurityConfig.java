package com.hms.usersmicroservice.config;

import com.hms.usersmicroservice.service.CustomUserDetailsService;
import com.hms.usersmicroservice.util.JwtUtil;
import com.hms.usersmicroservice.config.JwtRequestFilter;
import com.hms.usersmicroservice.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/authenticate").permitAll()
                                .requestMatchers("/register").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("DOCTOR","RECEPTIONIST")
                                .requestMatchers(HttpMethod.GET, "/api/clinics/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/clinics/**").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.PUT, "/api/clinics/**").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/clinics/**").hasRole("DOCTOR")
                                .requestMatchers("/patients").hasAnyRole("RECEPTIONIST", "DOCTOR")
                                .requestMatchers(HttpMethod.DELETE, "/patients/**").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.POST, "/medicalrecords").hasRole("DOCTOR")
                                .requestMatchers("/medicalrecords/**").hasAnyRole("RECEPTIONIST", "DOCTOR")
                                .requestMatchers("/billings/**").hasAnyRole("RECEPTIONIST", "DOCTOR")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout -> logout.disable()); // Disable default logout handling

        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, customUserDetailsService, tokenBlacklistService);
    }
}
