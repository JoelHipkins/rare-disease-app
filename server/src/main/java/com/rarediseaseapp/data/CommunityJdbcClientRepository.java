package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommunityJdbcClientRepository implements CommunityRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String CREATE_COMMUNITY_SQL = "INSERT INTO communities (community_name, community_description, user_id, created_at, updated_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    private final String GET_ALL_COMMUNITIES_SQL = "SELECT c.community_id, c.community_name, c.community_description, u.username AS creator_name, COUNT(cm.user_id) AS total_members, u.id AS user_id, c.created_at, c.updated_at FROM communities c JOIN users u ON c.user_id = u.id LEFT JOIN community_members cm ON c.community_id = cm.community_id GROUP BY c.community_id, c.community_name, c.community_description, u.username, u.id, c.created_at, c.updated_at";
    private final String GET_COMMUNITY_BY_ID_SQL = "SELECT c.community_id, c.community_name, c.community_description, u.username AS creator_name FROM communities c JOIN users u ON c.user_id = u.id WHERE c.community_id = ?";
    private static final String GET_TOTAL_MEMBERS_SQL = "SELECT COUNT(*) FROM community_members WHERE community_id = ?";

    public CommunityJdbcClientRepository() {
    }

    public void saveCommunity(String communityName, String communityDescription, int userId) {
        this.jdbcTemplate.update("INSERT INTO communities (community_name, community_description, user_id, created_at, updated_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", new Object[]{communityName, communityDescription, userId});
    }

    public List<Community> findAll() {
        return this.jdbcTemplate.query("SELECT c.community_id, c.community_name, c.community_description, u.username AS creator_name, COUNT(cm.user_id) AS total_members, u.id AS user_id, c.created_at, c.updated_at FROM communities c JOIN users u ON c.user_id = u.id LEFT JOIN community_members cm ON c.community_id = cm.community_id GROUP BY c.community_id, c.community_name, c.community_description, u.username, u.id, c.created_at, c.updated_at", new CommunityMapper());
    }

    public Community getCommunityById(int communityId) {
        try {
            return (Community)this.jdbcTemplate.queryForObject("SELECT c.community_id, c.community_name, c.community_description, u.username AS creator_name FROM communities c JOIN users u ON c.user_id = u.id WHERE c.community_id = ?", new Object[]{communityId}, new CommunityMapper());
        } catch (Exception var3) {
            return null;
        }
    }

    public int getTotalMembers(int communityId) {
        return (Integer)this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM community_members WHERE community_id = ?", new Object[]{communityId}, Integer.class);
    }
}
