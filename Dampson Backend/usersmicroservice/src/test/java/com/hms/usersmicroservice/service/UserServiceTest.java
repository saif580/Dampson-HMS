package com.hms.usersmicroservice.service;

import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername_UserFound() {
        // Given
        String username = "testuser";
        User mockUser = new User(1L, "testuser", "password", "ROLE_USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // When
        User foundUser = userService.findByUsername(username);

        // Then
        assertNotNull(foundUser);
        assertEquals(mockUser.getUsername(), foundUser.getUsername());
        assertEquals(mockUser.getPassword(), foundUser.getPassword());
        assertEquals(mockUser.getRole(), foundUser.getRole());
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        // Given
        String username = "unknownuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(RuntimeException.class, () -> userService.findByUsername(username));
    }

    @Test
    public void testFindById_UserFound() {
        // Given
        Long userId = 1L;
        User mockUser = new User(userId, "testuser", "password", "ROLE_USER");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When
        User foundUser = userService.findById(userId);

        // Then
        assertNotNull(foundUser);
        assertEquals(mockUser.getUserId(), foundUser.getUserId());
        assertEquals(mockUser.getUsername(), foundUser.getUsername());
        assertEquals(mockUser.getPassword(), foundUser.getPassword());
        assertEquals(mockUser.getRole(), foundUser.getRole());
    }

    @Test
    public void testFindById_UserNotFound() {
        // Given
        Long userId = 100L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(RuntimeException.class, () -> userService.findById(userId));
    }

    @Test
    public void testGetAllUsers() {
        // Given
        User user1 = new User(1L, "user1", "password1", "ROLE_USER");
        User user2 = new User(2L, "user2", "password2", "ROLE_ADMIN");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> userList = userService.getAllUsers();

        // Then
        assertEquals(2, userList.size());
        assertEquals(user1, userList.get(0));
        assertEquals(user2, userList.get(1));
    }

    @Test
    public void testSaveUser() {
        // Given
        User userToSave = new User(null, "newuser", "password", "ROLE_USER");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(1L); // Simulate saving with generated ID
            return user;
        });

        // When
        User savedUser = userService.saveUser(userToSave);

        // Then
        assertNotNull(savedUser.getUserId());
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("ROLE_USER", savedUser.getRole());
    }

    @Test
    public void testDeleteUser() {
        // Given
        Long userId = 1L;

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }
}
