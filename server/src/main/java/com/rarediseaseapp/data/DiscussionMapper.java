package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Participation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscussionMapper implements RowMapper<Participation> {
    public DiscussionMapper() {
    }

    public Participation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Participation participation = new Participation();
        participation.setPostId(rs.getInt("post_id"));
        participation.setUserId(rs.getInt("user_id"));
        participation.setContent(rs.getString("content"));
        participation.setUsername(rs.getString("username"));
        return participation;
    }
}

