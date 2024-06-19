package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.service.CustomUserDetailsService;
import com.hms.usersmicroservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping
public class AuthController {
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/authenticate")
    public Map<String, String> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            System.out.println("Authenticating user: " + authRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            System.out.println("Authentication successful for user: " + authRequest.getUsername());

            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
            System.out.println("Loaded user details for user: " + authRequest.getUsername());

            final String jwt = jwtUtil.generateToken(userDetails);
            System.out.println("Generated JWT: " + jwt);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            System.out.println("User Role: " + role);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            response.put("role", role);

            return response;
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error during authentication", e);
        }
    }

    @PostMapping("register")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public User registerUser(@RequestBody User user) {
        logger.info("Registering user: " + user.getUsername());
        if (userDetailsService.userExists(user.getUsername())) {
            logger.warning("Username already exists: " + user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("User registered successfully: " + user.getUsername());
        return userDetailsService.saveUser(user);
    }
}
