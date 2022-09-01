package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsById(Long postId);

    Optional<Post> findById(Long postId);

    Page<Post> findAll(List<String> tags, Pageable pageable);

    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}
