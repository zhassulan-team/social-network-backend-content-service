package kata.academy.eurekacontentservice.service.abst.entity;

import kata.academy.eurekacontentservice.model.entity.Post;

import java.util.Optional;

public interface PostService {
    Post addPost(Post post);
    Post updatePost(Post post);
    void deletePostById(Long id);
    Optional<Post> findPostByPostId(Long postId);
    Optional<Post> findPostByUserId (Long userId);

}
