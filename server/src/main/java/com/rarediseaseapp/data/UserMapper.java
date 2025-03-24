package com.rarediseaseapp.data;

import com.rarediseaseapp.models.User;

public class UserMapper implements RowMapper<User> {
    public UserMapper() {
    }

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
