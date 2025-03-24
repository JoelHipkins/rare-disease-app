package com.rarediseaseapp.controllers;

import com.rarediseaseapp.domain.PostService;
import com.rarediseaseapp.models.Answer;
import com.rarediseaseapp.models.Comment;
import com.rarediseaseapp.models.Participation;
import com.rarediseaseapp.models.Post;
import com.rarediseaseapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(
        origins = {"http://localhost:5173"}
)
@RestController
@RequestMapping({"/api/posts"})
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JwtUtil jwtUtil;

    public PostController() {
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = this.postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Object> createPost(@RequestHeader("Authorization") String authHeader, @RequestBody Post post) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");
                int userId = this.jwtUtil.extractUserId(token).intValue();
                System.out.println("Extracted userId: " + userId);
                post.setUserId(userId);
                if (post.getContent() != null && !post.getContent().trim().isEmpty()) {
                    boolean isPostCreated = this.postService.createPost(post);
                    return isPostCreated ? new ResponseEntity("Post created successfully!", HttpStatus.CREATED) : new ResponseEntity("Failed to create post", HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    return new ResponseEntity("Post content cannot be empty", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("Missing or invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/{postId}/like"})
    public ResponseEntity<Map<String, String>> likePost(@PathVariable int postId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int userId = this.jwtUtil.extractUserId(token).intValue();
            this.postService.likePost(postId, userId);
            return new ResponseEntity(Map.of("message", "Post liked successfully"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(Map.of("message", "An error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/{postId}/comment"})
    public ResponseEntity<Map<String, String>> commentOnPost(@PathVariable int postId, @RequestBody Comment comment, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int userId = this.jwtUtil.extractUserId(token).intValue();
            if (comment.getContent().isEmpty()) {
                return new ResponseEntity(Map.of("message", "Content cannot be empty"), HttpStatus.BAD_REQUEST);
            } else {
                boolean isCommentAdded = this.postService.commentOnPost(postId, userId, comment.getContent());
                return isCommentAdded ? new ResponseEntity(Map.of("message", "Comment added successfully"), HttpStatus.CREATED) : new ResponseEntity(Map.of("message", "Failed to add comment"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(Map.of("message", "An error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/{postId}/answer"})
    public ResponseEntity<Map<String, String>> answerQuestion(@PathVariable int postId, @RequestBody Answer answer, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int userId = this.jwtUtil.extractUserId(token).intValue();
            if (answer.getContent().isEmpty()) {
                return new ResponseEntity(Map.of("message", "Content cannot be empty"), HttpStatus.BAD_REQUEST);
            } else {
                boolean isAnswerAdded = this.postService.answerQuestion(postId, userId, answer.getContent());
                return isAnswerAdded ? new ResponseEntity(Map.of("message", "Answer added successfully"), HttpStatus.CREATED) : new ResponseEntity(Map.of("message", "Failed to add answer"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(Map.of("message", "An error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/{postId}/participate"})
    public ResponseEntity<Map<String, String>> participateInDiscussion(@PathVariable int postId, @RequestBody Participation participation, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int userId = this.jwtUtil.extractUserId(token).intValue();
            if (participation.getContent().isEmpty()) {
                return new ResponseEntity(Map.of("message", "Content cannot be empty"), HttpStatus.BAD_REQUEST);
            } else {
                boolean isParticipationAdded = this.postService.participateInDiscussion(postId, userId, participation.getContent());
                return isParticipationAdded ? new ResponseEntity(Map.of("message", "Participated successfully"), HttpStatus.CREATED) : new ResponseEntity(Map.of("message", "Failed to participate"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(Map.of("message", "An error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/{postId}/comments"})
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable int postId) {
        try {
            List<Comment> comments = this.postService.getCommentsByPostId(postId);
            return comments.isEmpty() ? new ResponseEntity(new ArrayList(), HttpStatus.OK) : new ResponseEntity(comments, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/{postId}/answers"})
    public ResponseEntity<List<Answer>> getAnswersByPostId(@PathVariable int postId) {
        try {
            List<Answer> answers = this.postService.getAnswersByPostId(postId);
            return answers.isEmpty() ? new ResponseEntity(new ArrayList(), HttpStatus.OK) : new ResponseEntity(answers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/{postId}/discussions"})
    public ResponseEntity<List<Participation>> getDiscussionsByPostId(@PathVariable int postId) {
        try {
            List<Participation> discussions = this.postService.getDiscussionsByPostId(postId);
            return discussions.isEmpty() ? new ResponseEntity(new ArrayList(), HttpStatus.OK) : new ResponseEntity(discussions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
