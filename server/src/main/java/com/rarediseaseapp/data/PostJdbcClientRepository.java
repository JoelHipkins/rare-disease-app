package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Post;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostJdbcClientRepository implements PostRepository {
    private final JdbcClient client;
    private static final String INSERT_POST_SQL = "INSERT INTO posts (content, type, user_id) VALUES (:content, :type, :userId)";
    private static final String SELECT_ALL_POSTS = "SELECT p.id, p.content, p.type, p.user_id, p.created_at, u.username, (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.id) AS likes_count, (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.id) AS comments_count, (SELECT COUNT(*) FROM answers a WHERE a.post_id = p.id) AS answers_count, (SELECT COUNT(*) FROM discussions d WHERE d.post_id = p.id) AS participations_count FROM posts p JOIN users u ON p.user_id = u.id";
    private static final String SELECT_POST_BY_ID = "SELECT p.id, p.content, p.type, p.user_id, p.created_at, u.username FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = :id";

    public PostJdbcClientRepository(JdbcClient client) {
        this.client = client;
    }

    public int savePost(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = this.client.sql("INSERT INTO posts (content, type, user_id) VALUES (:content, :type, :userId)").param("content", post.getContent()).param("type", post.getType()).param("userId", post.getUserId()).update(keyHolder, new String[]{"id"});
        System.out.println("Post inserted, rows affected: " + rows);
        return rows;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = this.client.sql("SELECT p.id, p.content, p.type, p.user_id, p.created_at, u.username, (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.id) AS likes_count, (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.id) AS comments_count, (SELECT COUNT(*) FROM answers a WHERE a.post_id = p.id) AS answers_count, (SELECT COUNT(*) FROM discussions d WHERE d.post_id = p.id) AS participations_count FROM posts p JOIN users u ON p.user_id = u.id").query(new PostMapper()).list();
        System.out.println("Fetched posts count: " + posts.size());
        return posts;
    }

    public Optional<Post> findById(int id) {
        return this.client.sql("SELECT p.id, p.content, p.type, p.user_id, p.created_at, u.username FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = :id").param("id", id).query(new PostMapper()).optional();
    }
}
