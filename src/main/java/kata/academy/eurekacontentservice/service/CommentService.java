package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment addComment(Comment comment);

    Comment updateComment(Comment comment);

    void deleteById(Long commentId);

    Optional<Comment> findByIdAndPostIdAndUserId(Long commentId, Long postId, Long userId);

    List<Long> findAllIdsByPostId(Long postId);

    boolean existsByIdAndPostIdAndUserId(Long commentId, Long postId, Long userId);

    boolean existsById(Long commentId);
}
