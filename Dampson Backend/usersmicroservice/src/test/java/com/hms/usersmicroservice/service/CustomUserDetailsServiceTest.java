package com.hms.usersmicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.repository.UserRepository;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        // Arrange
    	long UserId = 1;
        String username = "testUser";
        String password = "password";
        String role = "USER";
        User user = new User(UserId, username, password, role);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role)));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testSaveUser() {
        // Arrange
    	long userId = 1;
        String username = "newUser";
        String password = "password";
        String role = "ADMIN";
        User user = new User(userId,username, password, role);

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userDetailsService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(username, savedUser.getUsername());
        assertEquals(password, savedUser.getPassword());
        assertEquals(role, savedUser.getRole());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUserExists() {
        // Arrange
        String existingUsername = "existingUser";
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(new User()));

        String nonExistingUsername = "nonExistingUser";
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Act and Assert
        assertTrue(userDetailsService.userExists(existingUsername));
        assertFalse(userDetailsService.userExists(nonExistingUsername));

        verify(userRepository, times(1)).findByUsername(existingUsername);
        verify(userRepository, times(1)).findByUsername(nonExistingUsername);
    }
}
