package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.model.entity.Comment;

import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.CommentRepository;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.entity.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment, Long postId, Long userId) {
        Post post = postRepository.findPostById(postId);
        comment.setPost(post);
        comment.setUserId(userId);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Comment comment, Long postId, Long userId) {
        comment.setText(comment.getText());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }


    @Override
    public Optional<Comment> findByUserIdAndPostId(Long userId, Long postId) {
        return commentRepository.findByUserIdAndPostId(userId, postId);
    }

    @Override
    public Optional<Comment> findByUserIdAndPostIdAndCommentId(Long userId, Long postId, Long commentId) {
        return commentRepository.findByUserIdAndPostIdAndCommentId(userId, postId, commentId);
    }
}
