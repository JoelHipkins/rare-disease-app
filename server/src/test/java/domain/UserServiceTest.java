package domain;

import com.rarediseaseapp.data.UserRepository;
import com.rarediseaseapp.domain.UserService;
import com.rarediseaseapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User rawUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rawUser = new User();
        rawUser.setUsername("testuser");
        rawUser.setEmail("test@example.com");
        rawUser.setPassword("plaintext123");
        rawUser.setRole("ROLE_USER");
    }

    @Test
    void saveUser_shouldHashPasswordAndCreateUser() {
        when(userRepository.create(any(User.class)))
                .thenAnswer(invocation -> {
                    User arg = invocation.getArgument(0);
                    arg.setId(1);
                    return arg;
                });

        User saved = userService.saveUser(rawUser);

        assertNotNull(saved);
        assertTrue(saved.getPassword().startsWith("$2a$"));
        assertEquals("testuser", saved.getUsername());
        verify(userRepository, times(1)).create(any(User.class));
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = userService.existsByEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    void findByEmail_shouldReturnUser() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(rawUser));

        Optional<User> result = userService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void validatePassword_shouldReturnTrueIfMatch() {
        String hashed = userService.saveUser(rawUser).getPassword();
        assertTrue(userService.validatePassword("plaintext123", hashed));
    }
}
