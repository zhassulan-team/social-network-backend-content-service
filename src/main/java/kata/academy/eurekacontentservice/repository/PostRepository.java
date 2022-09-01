package kata.academy.eurekacontentservice.repository;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByIdAndUserId(Long postId, Long userId);

    Page<Post> findAll(List<String> tags, Pageable pageable);

    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}
