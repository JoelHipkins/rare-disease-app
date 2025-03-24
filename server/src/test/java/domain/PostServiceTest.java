package domain;

import com.rarediseaseapp.data.PostRepository;
import com.rarediseaseapp.domain.PostService;
import com.rarediseaseapp.models.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PostService postService;

    private Post mockPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockPost = new Post();
        mockPost.setId(1);
        mockPost.setContent("Test content");
        mockPost.setType("question");
        mockPost.setUserId(2);
        mockPost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mockPost.setUsername("testuser");
    }

    @Test
    void createPost_shouldReturnTrueIfSaved() {
        when(postRepository.savePost(any(Post.class))).thenReturn(1);

        boolean result = postService.createPost(mockPost);

        assertTrue(result);
    }

    @Test
    void getAllPosts_shouldReturnListOfPosts() {
        when(postRepository.getAllPosts()).thenReturn(List.of(mockPost));

        List<Post> posts = postService.getAllPosts();

        assertEquals(1, posts.size());
    }

    @Test
    void findPostById_shouldReturnOptionalPost() {
        when(postRepository.findById(1)).thenReturn(Optional.of(mockPost));

        Optional<Post> result = postService.findPostById(1);

        assertTrue(result.isPresent());
    }

    @Test
    void likePost_shouldUpdateLikesAndReturnTrue() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        boolean result = postService.likePost(1, 2);

        assertTrue(result);
    }

    @Test
    void commentOnPost_shouldInsertComment() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        assertTrue(postService.commentOnPost(1, 2, "Nice post!"));
    }

    @Test
    void answerQuestion_shouldInsertAnswer() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        assertTrue(postService.answerQuestion(1, 2, "Answer here"));
    }

    @Test
    void participateInDiscussion_shouldInsertParticipation() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        assertTrue(postService.participateInDiscussion(1, 2, "Let's talk"));
    }
}
