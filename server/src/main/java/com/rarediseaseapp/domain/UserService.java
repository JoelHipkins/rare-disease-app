package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserJdbcClientRepository;
import com.rarediseaseapp.models.User;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

<<<<<<< HEAD
    private final UserJdbcClientRepository userJdbcClientRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserJdbcClientRepository userJdbcClientRepository) {
        this.userJdbcClientRepository = userJdbcClientRepository;
    }

    public void registerUser(String name, String email, String password, String role) {
        if (userJdbcClientRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(0, name, email, hashedPassword, role, null);
        userJdbcClientRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> userOptional = userJdbcClientRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Optional<User> getUserByEmail(String email) {
        return userJdbcClientRepository.findByEmail(email);
    }

    public void updateProfile(int userId, String name, String email, String password, String role) {
        Optional<User> existingUser = userJdbcClientRepository.findByEmail(email);

        if (existingUser.isPresent() && existingUser.get().getUserId() != userId) {
            throw new RuntimeException("Email already in use by another user!");
        }

        String hashedPassword = password.isEmpty() ? existingUser.get().getPassword() : passwordEncoder.encode(password);
        userJdbcClientRepository.updateUser(userId, name, email, hashedPassword, role);
    }

    public void deleteUser(int userId) {
        userJdbcClientRepository.deleteUser(userId);
=======
    public UserService() {
    }

    public User saveUser(User user) {
        try {
            return (User)this.userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Error saving user to the database: " + e.getMessage());
        }
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    public User findByEmail(String email) {
        return (User)this.userRepository.findByEmail(email).orElse((User)null);
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97
    }
}

