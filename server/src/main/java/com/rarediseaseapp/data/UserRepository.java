package com.rarediseaseapp.data;

<<<<<<< HEAD

import com.rarediseaseapp.models.User;

import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);

    User create(User user);

=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.rarediseaseapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
>>>>>>> bfa8440601ec4af7e6683a086b241d913aba2a97
    Optional<User> findByEmail(String email);
}