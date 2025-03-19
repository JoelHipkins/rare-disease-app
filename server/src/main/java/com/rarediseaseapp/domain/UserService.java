package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.UserRepository;
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
    }
}

