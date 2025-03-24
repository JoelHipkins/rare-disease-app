package com.rarediseaseapp.data;


import com.rarediseaseapp.models.User;

import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);

    User create(User user);

    Optional<User> findByEmail(String email);
}