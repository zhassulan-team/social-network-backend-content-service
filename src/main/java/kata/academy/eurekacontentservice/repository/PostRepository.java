package kata.academy.eurekacontentservice.repository;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByIdAndUserId(Long postId, Long userId);

    Optional<Post> findByIdAndUserId(Long postId, Long userId);

    @Query("""
            SELECT c.userId
            FROM Post c
            WHERE c.id = :postId
                                """)
    Optional<Long> getUserIdById(Long postId);
}
