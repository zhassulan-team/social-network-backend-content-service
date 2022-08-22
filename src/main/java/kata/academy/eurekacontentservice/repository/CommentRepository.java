package kata.academy.eurekacontentservice.repository;

import kata.academy.eurekacontentservice.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUserIdAndPostId(Long userId, Long postId);

    Optional<Comment> findByUserIdAndPostIdAndCommentId(Long userId, Long postId, Long commentId);

}
