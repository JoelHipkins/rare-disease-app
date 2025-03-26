package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Community;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommunityMapper implements RowMapper<Community> {
    public CommunityMapper() {
    }

    public Community mapRow(ResultSet rs, int rowNum) throws SQLException {
        Community community = new Community();
        community.setCommunityId(rs.getInt("community_id"));
        community.setCommunityName(rs.getString("community_name"));
        community.setCommunityDescription(rs.getString("community_description"));
        community.setCreatorName(rs.getString("creator_name"));
        community.setUserId(rs.getInt("user_id"));
        community.setCreatedAt(rs.getTimestamp("created_at"));
        community.setUpdatedAt(rs.getTimestamp("updated_at"));
        return community;
    }
}
