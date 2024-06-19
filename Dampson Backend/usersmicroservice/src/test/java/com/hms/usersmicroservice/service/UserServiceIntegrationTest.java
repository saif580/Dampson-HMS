package com.hms.usersmicroservice.service;

import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.repository.UserRepository;
import com.hms.usersmicroservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
@SpringBootTest // Use SpringBootTest for full integration testing
@ActiveProfiles("test") // Use a test profile if needed
@Transactional // Rollback transactions after each test
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void testFindByUsername() {
        // Arrange
    	long userId = 1;
        String username = "testuser";
        User user = new User(userId,username, "password", "USER");
        userRepository.save(user);

        // Act
        User foundUser = userService.findByUsername(username);

        // Assert
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    public void testFindById() {
        // Arrange
        User user = new User(1L,"testuser", "password", "USER");
        userRepository.save(user);

        // Act
        User foundUser = userService.findById(user.getUserId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getUserId(), foundUser.getUserId());
    }

    @Test
    public void testSaveUser() {
        // Arrange
        User user = new User(1L,"newuser", "password", "USER");

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        User user = new User(2L,"todelete", "password", "USER");
        userRepository.save(user);

        // Act
        userService.deleteUser(user.getUserId());

        // Assert
        assertNull(userRepository.findById(user.getUserId()).orElse(null));
    }

    @Test
    public void testGetAllUsers() {
        // Assuming you have inserted 2 test users in setUp method
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size(), "Expected 2 users in the database");
    }

}
