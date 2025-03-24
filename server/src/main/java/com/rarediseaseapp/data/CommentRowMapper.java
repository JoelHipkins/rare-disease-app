package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CommentRowMapper implements RowMapper<Comment> {
    public CommentRowMapper() {
    }

    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setContent(rs.getString("content"));
        comment.setUsername(rs.getString("username"));
        return comment;
    }
}
