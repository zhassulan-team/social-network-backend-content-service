package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Page<Post> getAllPosts(List<String> tags, Pageable pageable);
    Page<Post> getAllPostsByUserId(Long userId, List<String> tags, Pageable pageable);
    Page<Post> getTopPostsByCount(Integer count, Pageable pageable);

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsById(Long postId);

    Optional<Post> findById(Long postId);
}
