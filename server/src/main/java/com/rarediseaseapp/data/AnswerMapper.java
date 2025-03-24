package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Answer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerMapper implements RowMapper<Answer> {
    public AnswerMapper() {
    }

    public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Answer answer = new Answer();
        answer.setPostId(rs.getInt("post_id"));
        answer.setUserId(rs.getInt("user_id"));
        answer.setContent(rs.getString("content"));
        answer.setUsername(rs.getString("username"));
        return answer;
    }
}
