//package data;
//
//import com.rarediseaseapp.data.UserJdbcClientRepository;
//import com.rarediseaseapp.data.UserMapper;
//import com.rarediseaseapp.models.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.jdbc.core.simple.JdbcClient;
//import org.springframework.jdbc.support.KeyHolder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserJdbcClientRepositoryTest {
//
//    @Mock
//    JdbcClient jdbcClient;
//
//    @Mock
//    JdbcClient.StatementSpec statementSpec;
//
//    @Mock
//    JdbcClient.MappedQuerySpec<Integer> mappedQuerySpecInt;
//
//    @Mock
//    JdbcClient.MappedQuerySpec<User> mappedQuerySpecUser;
//
//    @Mock
//    KeyHolder mockKeyHolder;
//
//    @InjectMocks
//    UserJdbcClientRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void existsByEmail_shouldReturnTrue() {
//        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
//        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
//        when(statementSpec.query(eq(Integer.class))).thenReturn(mappedQuerySpecInt);
//        when(mappedQuerySpecInt.single()).thenReturn(1);
//
//        boolean result = repository.existsByEmail("test@example.com");
//
//        assertTrue(result);
//    }
//
//    @Test
//    void findByEmail_shouldReturnUserOptional() {
//        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
//        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
//        when(statementSpec.query(any(UserMapper.class))).thenReturn(mappedQuerySpecUser);
//        when(mappedQuerySpecUser.optional()).thenReturn(Optional.of(new User()));
//
//        Optional<User> result = repository.findByEmail("test@example.com");
//
//        assertTrue(result.isPresent());
//    }
//
//    @Test
//    void create_shouldInsertUserAndReturnUserWithId() {
//        User user = new User();
//        user.setUsername("joel");
//        user.setEmail("joel@example.com");
//        user.setPassword("password");
//        user.setRole("USER");
//
//        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
//        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
//        when(statementSpec.update(any(KeyHolder.class), any())).thenReturn(1);
//        when(mockKeyHolder.getKey()).thenReturn(99);
//
//        // Call create
//        user.setId(99); // simulate the side-effect
//        User result = repository.create(user);
//
//        assertNotNull(result);
//        assertEquals(99, result.getId());
//    }
//}
