package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserJdbcClientRepository;
import com.rarediseaseapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
<<<<<<< HEAD
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
=======
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

<<<<<<< HEAD
    private UserJdbcClientRepository userJdbcClientRepository;
=======
    @Mock
    private UserRepository userRepository;

    @InjectMocks
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
<<<<<<< HEAD
        userJdbcClientRepository = mock(UserJdbcClientRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userJdbcClientRepository);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User("John Doe", "johndoe@example.com", "password123", "Patient");
        when(userJdbcClientRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
=======
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
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

        User savedUser = userService.saveUser(user);

<<<<<<< HEAD
        verify(userJdbcClientRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_Fail_DuplicateEmail() {
        User existingUser = new User(1, "John Doe", "johndoe@example.com", "password123", "Patient", null);
        when(userJdbcClientRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
=======
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser_ThrowsException() {
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

        Exception exception = assertThrows(IllegalStateException.class, () -> userService.saveUser(user));

        assertTrue(exception.getMessage().contains("Error saving user to the database"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
<<<<<<< HEAD
    void testLoginUser_Success() {
        String rawPassword = "password123";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(1, "Jane Doe", "janedoe@example.com", hashedPassword, "Pharma", null);

        when(userJdbcClientRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
=======
    void testExistsByEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

        boolean exists = userService.existsByEmail("test@example.com");

        assertTrue(exists);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testExistsByEmail_UserDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

<<<<<<< HEAD
        when(userJdbcClientRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
=======
        boolean exists = userService.existsByEmail("nonexistent@example.com");
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

        assertFalse(exists);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
<<<<<<< HEAD
    void testUpdateProfile_Success() {
        User existingUser = new User(1, "Jane Doe", "janedoe@example.com", "password123", "Pharma", null);
        when(userJdbcClientRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
=======
    void testFindByEmail_UserFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

        User foundUser = userService.findByEmail("test@example.com");

<<<<<<< HEAD
        verify(userJdbcClientRepository, times(1)).updateUser(anyInt(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateProfile_Fail_EmailAlreadyUsed() {
        User existingUser = new User(1, "Jane Doe", "janedoe@example.com", "password123", "Pharma", null);
        User anotherUser = new User(2, "John Smith", "johnsmith@example.com", "password456", "Patient", null);

        when(userJdbcClientRepository.findByEmail(anotherUser.getEmail())).thenReturn(Optional.of(anotherUser));
=======
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97

        User foundUser = userService.findByEmail("nonexistent@example.com");

<<<<<<< HEAD
        verify(userJdbcClientRepository, times(1)).deleteUser(1);
=======
        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97
    }
}
