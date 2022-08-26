package kata.academy.eurekacontentservice.repository;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByIdAndUserId(Long postId, Long userId);
}
