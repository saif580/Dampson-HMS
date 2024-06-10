package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("Logging out user with token: " + authorizationHeader);
        String token = authorizationHeader.substring(7); // Extract token from "Bearer <token>"
        tokenBlacklistService.blacklistToken(token);
        SecurityContextHolder.clearContext();
        return "You have been logged out successfully";
    }
}
