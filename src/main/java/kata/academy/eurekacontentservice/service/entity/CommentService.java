package kata.academy.eurekacontentservice.service.entity;

import kata.academy.eurekacontentservice.model.entity.Comment;

import java.util.Optional;

public interface CommentService {

    Comment addComment(Comment comment, Long postId, Long userId);

    Comment updateComment(Comment comment, Long postId, Long userId);

    void deleteComment(Comment comment);

    Optional<Comment> findByUserIdAndPostId(Long userId, Long postId);

    Optional<Comment> findByUserIdAndPostIdAndId(Long userId, Long postId, Long commentId);
}
