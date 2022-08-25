package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;

public interface PostService {

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);
}
