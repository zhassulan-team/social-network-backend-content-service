package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.CommentRepository;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final LikeServiceFeignClient likeServiceFeignClient;

    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(Long postId) {
        List<Long> commentIds = commentRepository.findAllIdsByPostId(postId);
        commentRepository.deleteAllByIdInBatch(commentIds);
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndUserId(Long postId, Long userId) {
        return postRepository.existsByIdAndUserId(postId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long postId) {
        return postRepository.existsById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> findAll(List<String> tags, Pageable pageable) {
        return postRepository.findAll(tags, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> findAllByUserId(List<String> tags, Long userId, Pageable pageable) {
        return postRepository.findAllByUserId(tags, userId, pageable);
    }

    @Override
    public Page<Post> findAllByUserId(Long userId, Pageable pageable) {
        return postRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<Post> findAllByPostId(List<Long> ids, Pageable pageable) {
        return postRepository.findAllByPostId(ids, pageable);
    }

}
