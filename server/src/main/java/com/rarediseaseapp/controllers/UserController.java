package com.rarediseaseapp.controllers;

import com.rarediseaseapp.domain.UserService;
import com.rarediseaseapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {"http://localhost:5173"}
)
@RestController
@RequestMapping({"/api/users"})
public class UserController {
    @Autowired
    private UserService userService;

    public UserController() {
    }

    @PostMapping({"/register"})
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (this.userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists!");
        } else {
            this.userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
        }
    }

    @PostMapping({"/login"})
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        User existingUser = this.userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please register first!");
        } else {
            return existingUser.getPassword().equals(user.getPassword()) ? ResponseEntity.status(HttpStatus.OK).body("Welcome " + existingUser.getUsername() + "!") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect credentials!");
        }
    }
}