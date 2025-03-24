package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserJdbcClientRepository;
import com.rarediseaseapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserJdbcClientRepository userJdbcClientRepository;
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userJdbcClientRepository = mock(UserJdbcClientRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userJdbcClientRepository);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User("John Doe", "johndoe@example.com", "password123", "Patient");
        when(userJdbcClientRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole()));

        verify(userJdbcClientRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_Fail_DuplicateEmail() {
        User existingUser = new User(1, "John Doe", "johndoe@example.com", "password123", "Patient", null);
        when(userJdbcClientRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(existingUser.getName(), existingUser.getEmail(), existingUser.getPassword(), existingUser.getRole())
        );

        assertEquals("Email already exists!", exception.getMessage());
    }

    @Test
    void testLoginUser_Success() {
        String rawPassword = "password123";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(1, "Jane Doe", "janedoe@example.com", hashedPassword, "Pharma", null);

        when(userJdbcClientRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User loggedInUser = userService.loginUser(user.getEmail(), rawPassword);

        assertNotNull(loggedInUser);
        assertEquals(user.getEmail(), loggedInUser.getEmail());
    }

    @Test
    void testLoginUser_Fail_InvalidPassword() {
        String hashedPassword = passwordEncoder.encode("password123");
        User user = new User(1, "Jane Doe", "janedoe@example.com", hashedPassword, "Pharma", null);

        when(userJdbcClientRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User loggedInUser = userService.loginUser(user.getEmail(), "wrongpassword");

        assertNull(loggedInUser);
    }

    @Test
    void testUpdateProfile_Success() {
        User existingUser = new User(1, "Jane Doe", "janedoe@example.com", "password123", "Pharma", null);
        when(userJdbcClientRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.updateProfile(1, "Updated Name", "janedoe@example.com", "", "Patient"));

        verify(userJdbcClientRepository, times(1)).updateUser(anyInt(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateProfile_Fail_EmailAlreadyUsed() {
        User existingUser = new User(1, "Jane Doe", "janedoe@example.com", "password123", "Pharma", null);
        User anotherUser = new User(2, "John Smith", "johnsmith@example.com", "password456", "Patient", null);

        when(userJdbcClientRepository.findByEmail(anotherUser.getEmail())).thenReturn(Optional.of(anotherUser));

        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.updateProfile(1, "Updated Name", "johnsmith@example.com", "", "Patient")
        );

        assertEquals("Email already in use by another user!", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        assertDoesNotThrow(() -> userService.deleteUser(1));

        verify(userJdbcClientRepository, times(1)).deleteUser(1);
    }
}
