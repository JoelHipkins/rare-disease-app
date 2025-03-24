package com.rarediseaseapp.controllers;

import com.rarediseaseapp.domain.UserService;
import com.rarediseaseapp.models.User;
import com.rarediseaseapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173") // Allow CORS for frontend access
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // JWT utility for creating tokens

    // POST method for user registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Check if the email already exists
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists!"); // If user exists, return 400 with a message
        }

        // Save the user with the hashed password
        userService.saveUser(user);

        // Return a success message without JWT token after registration
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!"); // No token here
    }

    // POST method for user login
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {
        // Check if the user exists by email
        // Unwrap the Optional<User> using .orElse(null)
        User existingUser = userService.findByEmail(user.getEmail()).orElse(null);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please register first");
        }


        // Validate password using BCrypt
        if (userService.validatePassword(user.getPassword(), existingUser.getPassword())) {
            // Create JWT token after successful login
            String jwt = jwtUtil.createToken(existingUser);

            // Return success message with the JWT token
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Welcome " + existingUser.getUsername() + "!", "jwt", jwt));  // 200 OK, return JWT token and message
        } else {
            // If password is incorrect, return an error message with 400 status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect credentials!");  // 400 Bad Request
        }
    }
}