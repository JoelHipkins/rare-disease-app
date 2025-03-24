package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserJdbcClientRepository;
import com.rarediseaseapp.data.UserRepository;
import com.rarediseaseapp.models.User;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(hashedPassword);
        return this.userRepository.create(user);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean validatePassword(String proposedPassword, String existingPassword) {
        return BCrypt.verifyer().verify(proposedPassword.toCharArray(), existingPassword.toCharArray()).verified;
    }
}

