package com.rarediseaseapp.data;

import com.rarediseaseapp.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new user
    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password, role, createdAt) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getRole(), new Timestamp(System.currentTimeMillis()));
    }

    // Find a user by email
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, userRowMapper());
        return users.stream().findFirst();
    }

    // Find a user by ID
    public Optional<User> findById(int userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{userId}, userRowMapper());
        return users.stream().findFirst();
    }

    // Update user details
    public void updateUser(int userId, String name, String email, String password, String role) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE userId = ?";
        jdbcTemplate.update(sql, name, email, password, role, userId);
    }

    // Delete a user by ID
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE userId = ?";
        jdbcTemplate.update(sql, userId);
    }

    // Maps database rows to a User object
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getInt("userId"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getTimestamp("createdAt")
        );
    }
}
