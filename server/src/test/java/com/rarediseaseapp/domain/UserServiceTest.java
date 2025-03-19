package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserRepository;
import com.rarediseaseapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("securepassword");
        user.setRole("USER");
    }

    @Test
    void testSaveUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser_ThrowsException() {
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        Exception exception = assertThrows(IllegalStateException.class, () -> userService.saveUser(user));

        assertTrue(exception.getMessage().contains("Error saving user to the database"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testExistsByEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean exists = userService.existsByEmail("test@example.com");

        assertTrue(exists);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testExistsByEmail_UserDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        boolean exists = userService.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void testFindByEmail_UserFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User foundUser = userService.findByEmail("nonexistent@example.com");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
