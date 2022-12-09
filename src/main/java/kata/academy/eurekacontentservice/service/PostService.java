package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;

import java.util.Optional;


public interface PostService {

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsById(Long postId);

    Optional<Post> findById(Long postId);

    Optional<Post> findByIdAndUserId(Long postId, Long userId);
}
