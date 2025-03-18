package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserRepository;
import com.rarediseaseapp.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String name, String email, String password, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(0, name, email, hashedPassword, role, null);
        userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateProfile(int userId, String name, String email, String password, String role) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent() && existingUser.get().getUserId() != userId) {
            throw new RuntimeException("Email already in use by another user!");
        }

        String hashedPassword = password.isEmpty() ? existingUser.get().getPassword() : passwordEncoder.encode(password);
        userRepository.updateUser(userId, name, email, hashedPassword, role);
    }

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }
}
