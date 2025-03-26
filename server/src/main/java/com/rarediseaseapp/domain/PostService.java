package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.*;
import com.rarediseaseapp.models.Answer;
import com.rarediseaseapp.models.Comment;
import com.rarediseaseapp.models.Participation;
import com.rarediseaseapp.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PostService() {
    }

    public boolean createPost(Post post) {
        int rowsAffected = this.postRepository.savePost(post);
        return rowsAffected > 0;
    }

    public List<Post> getAllPosts() {
        return this.postRepository.getAllPosts();
    }

    public Optional<Post> findPostById(int id) {
        return this.postRepository.findById(id);
    }

    public List<Post> getPostsForMedicos() {
        String sql = "SELECT p.id, p.content, p.type, p.user_id, p.created_at, u.username, (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.id) AS likes_count, (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.id) AS comments_count, (SELECT COUNT(*) FROM answers a WHERE a.post_id = p.id) AS answers_count, (SELECT COUNT(*) FROM discussions d WHERE d.post_id = p.id) AS participations_count FROM posts p JOIN users u ON p.user_id = u.id WHERE u.role = 'doctor' OR u.role = 'pharma' ;";
        List<Post> posts = this.jdbcTemplate.query(sql, new PostMapper());
        return posts;
    }

    public boolean likePost(int postId, int userId) {
        String insertLikeSql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
        this.jdbcTemplate.update(insertLikeSql, new Object[]{postId, userId});
        return true;
    }

    public boolean commentOnPost(int postId, int userId, String content) {
        String insertCommentSql = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(insertCommentSql, new Object[]{postId, userId, content});
        return true;
    }

    public boolean answerQuestion(int postId, int userId, String content) {
        String insertAnswerSql = "INSERT INTO answers (post_id, user_id, content) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(insertAnswerSql, new Object[]{postId, userId, content});
        return true;
    }

    public boolean participateInDiscussion(int postId, int userId, String content) {
        String insertDiscussionSql = "INSERT INTO discussions (post_id, user_id, content) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(insertDiscussionSql, new Object[]{postId, userId, content});
        return true;
    }

    public List<Comment> getCommentsByPostId(int postId) {
        String sql = "SELECT c.content, u.username FROM comments c JOIN users u ON c.user_id = u.id WHERE c.post_id = ?";
        List<Comment> comments = this.jdbcTemplate.query(sql, new Object[]{postId}, new CommentMapper());
        return comments;
    }

    public List<Answer> getAnswersByPostId(int postId) {
        String query = "SELECT a.*, u.username FROM answers a JOIN users u ON a.user_id = u.id WHERE a.post_id = ?";
        return this.jdbcTemplate.query(query, new Object[]{postId}, new AnswerMapper());
    }

    public List<Participation> getDiscussionsByPostId(int postId) {
        String query = "SELECT d.*, u.username FROM discussions d JOIN users u ON d.user_id = u.id WHERE d.post_id = ?";
        return this.jdbcTemplate.query(query, new Object[]{postId}, new DiscussionMapper());
    }
}

