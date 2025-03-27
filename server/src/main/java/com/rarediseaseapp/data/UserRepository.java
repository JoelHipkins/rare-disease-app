package com.rarediseaseapp.data;


import java.util.Optional;
import com.rarediseaseapp.models.User;


public interface UserRepository {
    boolean existsByEmail(String email);

    User create(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(int id);

    User update(User user);
}