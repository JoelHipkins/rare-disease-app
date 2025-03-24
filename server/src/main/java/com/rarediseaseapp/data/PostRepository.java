package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    int savePost(Post post);

    List<Post> getAllPosts();

    Optional<Post> findById(int id);
}
