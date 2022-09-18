package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Page<Post> getAllPosts(List<String> tags, Pageable pageable);
    Page<Post> getAllPostsByUserId(List<String> tags, Long userId, Pageable pageable);
    Page<Post> getPostsByLikesAmount(Integer count, Pageable pageable);

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsById(Long postId);

    Optional<Post> findById(Long postId);
}
