package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.model.entity.Comment;

import kata.academy.eurekacontentservice.repository.CommentRepository;
import kata.academy.eurekacontentservice.service.entity.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    @Override
    public Comment addComment(Comment comment, Long postId, Long userId) {
        Post post = postRepository.findPostById(postId);
        comment.setPost(post);
        comment.setUserId(userId);
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment, Long postId, Long userId) {
        comment.setText(comment.getText());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    @Transactional (readOnly = true)
    public Optional<Comment> findByUserIdAndPostId(Long userId, Long postId) {
        return commentRepository.findByUserIdAndPostId(userId, postId);
    }

    @Override
    @Transactional (readOnly = true)
    public Optional<Comment> findByUserIdAndPostIdAndId(Long userId, Long postId, Long commentId) {
        return commentRepository.findByUserIdAndPostIdAndId(userId, postId, commentId);
    }
}
