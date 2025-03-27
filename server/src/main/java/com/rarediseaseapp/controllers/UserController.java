package com.rarediseaseapp.controllers;

import com.rarediseaseapp.domain.UserService;
import com.rarediseaseapp.models.User;
import com.rarediseaseapp.security.JwtUtil;
import com.rarediseaseapp.domain.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@CrossOrigin(
        origins = {"http://localhost:5173"}
)
@RestController
@RequestMapping({"/api/users"})
public class UserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private JwtUtil jwtUtil;

    public UserController() {
    }

    @PostMapping({"/register"})
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (this.userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists!");
        } else {
            this.userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        }
    }

    @PostMapping({"/login"})
    public ResponseEntity<Object> loginUser(@RequestBody User user) {
        User existingUser = (User)this.userService.findByEmail(user.getEmail()).orElse((User) null);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please register first");
        } else if (this.userService.validatePassword(user.getPassword(), existingUser.getPassword())) {
            String jwt = this.jwtUtil.createToken(existingUser);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Welcome " + existingUser.getUsername() + "!", "jwt", jwt));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect credentials!");
        }
    }

    @GetMapping({"/profile"})
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = this.jwtUtil.extractEmail(token);
            Optional<User> userOptional = this.userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body((User) null);
            } else {
                User user = (User)userOptional.get();
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/profile"})
    public ResponseEntity<User> updateUserProfile(@RequestBody Map<String, String> userDetails, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int userId = this.jwtUtil.extractUserId(token).intValue();
            Optional<User> userOptional = this.userService.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body((User) null);
            } else {
                User user = (User)userOptional.get();
                String newUsername = (String)userDetails.get("username");
                String newRole = (String)userDetails.get("role");
                if (newUsername != null && !newUsername.isEmpty()) {
                    user.setUsername(newUsername);
                }

                if (newRole != null && !newRole.isEmpty()) {
                    user.setRole(newRole);
                }

                this.userService.updateUser(user);
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((User) null);
        }
    }

    @DeleteMapping({"/delete"})
    public ResponseEntity<Map<String, String>> deleteUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int userId = this.jwtUtil.extractUserId(token).intValue();
            this.postService.deletePostsByUserId(userId);
            String deleteCommentsSql = "DELETE FROM comments WHERE user_id = ?";
            this.jdbcTemplate.update(deleteCommentsSql, new Object[]{userId});
            String deleteLikesSql = "DELETE FROM likes WHERE user_id = ?";
            this.jdbcTemplate.update(deleteLikesSql, new Object[]{userId});
            String deleteAnswersSql = "DELETE FROM answers WHERE user_id = ?";
            this.jdbcTemplate.update(deleteAnswersSql, new Object[]{userId});
            String deleteDiscussionsSql = "DELETE FROM discussions WHERE user_id = ?";
            this.jdbcTemplate.update(deleteDiscussionsSql, new Object[]{userId});
            String deleteCommunityMembersSql = "DELETE FROM community_members WHERE user_id = ?";
            this.jdbcTemplate.update(deleteCommunityMembersSql, new Object[]{userId});
            String deleteUserSql = "DELETE FROM users WHERE id = ?";
            this.jdbcTemplate.update(deleteUserSql, new Object[]{userId});
            Map<String, String> response = new HashMap();
            response.put("message", "User and related data deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap();
            errorResponse.put("message", "Error deleting user");
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}