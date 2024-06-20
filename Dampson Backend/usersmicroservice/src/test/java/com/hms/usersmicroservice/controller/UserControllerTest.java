package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "johndoe", "password123", "ROLE_USER");
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("johndoe", result.get(0).getUsername());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById() {
        when(userService.getUserById(anyLong())).thenReturn(user);

        User result = userController.getUserById(1L);

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    public void testCreateUser() {
        when(userService.saveUser(any(User.class))).thenReturn(user);

        User result = userController.createUser(user);

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);

        User result = userController.updateUser(1L, user);

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userService, times(1)).updateUser(anyLong(), any(User.class));
    }

    @Test
    public void testUpdatePassword_Success() {
        Map<String, String> passwords = new HashMap<>();
        passwords.put("currentPassword", "password123");
        passwords.put("newPassword", "newpassword456");

        doNothing().when(userService).updatePassword(anyString(), anyString(), anyString());

        ResponseEntity<?> response = userController.updatePassword("johndoe", passwords);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully", response.getBody());
        verify(userService, times(1)).updatePassword(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdatePassword_Failure() {
        Map<String, String> passwords = new HashMap<>();
        passwords.put("currentPassword", "wrongpassword");
        passwords.put("newPassword", "newpassword456");

        doThrow(new RuntimeException("Error updating password")).when(userService).updatePassword(anyString(), anyString(), anyString());

        ResponseEntity<?> response = userController.updatePassword("johndoe", passwords);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error updating password: Error updating password", response.getBody());
        verify(userService, times(1)).updatePassword(anyString(), anyString(), anyString());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userService).deleteUser(anyLong());

        userController.deleteUser(1L);

        verify(userService, times(1)).deleteUser(anyLong());
    }
}
