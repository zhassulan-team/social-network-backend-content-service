package kata.academy.eurekacontentservice.repository;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByIdAndUserId(Long postId, Long userId);

    @Query("SELECT p FROM Post p WHERE p.tags IN (:tags)")
    Page<Post> findAll(List<String> tags, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id IN (:ids)")
    Page<Post> findAllByPostId(List<Long> ids, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.tags IN (:tags)")
    Page<Post> findAllByUserId(List<String> tags, Long userId, Pageable pageable);

    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}
