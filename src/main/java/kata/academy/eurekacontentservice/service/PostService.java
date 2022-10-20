package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Page<Post> findAllByTags(List<String> tags, Pageable pageable);

    Page<Post> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable);

    Page<Post> findAllTopByCount(Integer count, Pageable pageable);

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsById(Long postId);

    Optional<Post> findById(Long postId);

    Optional<Post> findByIdAndUserId(Long postId, Long userId);

    Long findUserIdByPostId(Long postId);
}
