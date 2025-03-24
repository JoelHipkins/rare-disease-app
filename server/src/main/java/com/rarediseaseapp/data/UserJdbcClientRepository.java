package com.rarediseaseapp.data;

import com.rarediseaseapp.models.User;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class UserJdbcClientRepository implements UserRepository {
    private final JdbcClient client;

    public UserJdbcClientRepository(JdbcClient client) {
        this.client = client;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = (Integer)this.client.sql("SELECT COUNT(*) FROM users WHERE email = ?").param(email).query(Integer.class).single();
        return count != null && count > 0;
    }

    public User create(User user) {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (:username, :email, :password, :role)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = this.client.sql("INSERT INTO users (username, email, password, role) VALUES (:username, :email, :password, :role)").param("username", user.getUsername()).param("email", user.getEmail()).param("password", user.getPassword()).param("role", user.getRole()).update(keyHolder, new String[]{"id"});
        if (rowsAffected <= 0) {
            return null;
        } else {
            user.setId(keyHolder.getKey().intValue());
            return user;
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return this.client.sql("SELECT * FROM users WHERE email = ?").param(email).query(new UserMapper()).optional();
    }
}

