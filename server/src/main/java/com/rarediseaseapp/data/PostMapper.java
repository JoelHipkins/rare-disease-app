package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<Post> {
    public PostMapper() {
    }

    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setContent(rs.getString("content"));
        post.setType(rs.getString("type"));
        post.setUserId(rs.getInt("user_id"));
        post.setUsername(rs.getString("username"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setLikesCount(rs.getInt("likes_count"));
        post.setCommentsCount(rs.getInt("comments_count"));
        if (post.getType().equals("question")) {
            post.setAnswersCount(rs.getInt("answers_count"));
        }

        if (post.getType().equals("discussion")) {
            post.setParticipationsCount(rs.getInt("participations_count"));
        }

        return post;
    }
}
