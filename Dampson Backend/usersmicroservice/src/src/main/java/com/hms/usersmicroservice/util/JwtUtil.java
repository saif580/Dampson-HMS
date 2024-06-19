package com.hms.usersmicroservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());
    private final String SECRET_KEY = "my-very-strong-secret-key-that-is-at-least-32-characters-long";

    public String extractUsername(String token) {
        logger.info("Extracting username from token: " + token);
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        logger.info("Extracting expiration from token: " + token);
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        logger.info("Extracted claims from token: " + claims);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
        logger.info("Extracted all claims: " + claims);
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        Boolean isExpired = extractExpiration(token).before(new Date());
        logger.info("Token expiration check: " + isExpired);
        return isExpired;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, userDetails.getUsername());
        logger.info("Generated JWT Token: " + token);
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
        logger.info("Created token with subject: " + subject + ", claims: " + claims);
        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        Boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        logger.info("Validating token for user: " + username + ", token valid: " + isValid);
        return isValid;
    }
}
