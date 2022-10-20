package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.CommentService;
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
    private final CommentService commentService;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Transactional(readOnly = true)
    @Override
    public Page<Post> findAllByTags(List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return postRepository.findAll(pageable);
        }
        return postRepository.findAllDistinctByTagsIn(tags, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return postRepository.findAllByUserId(userId, pageable);
        }
        return postRepository.findAllDistinctByUserIdAndTagsIn(userId, tags, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> findAllTopByCount(Integer count, Pageable pageable) {
        List<Long> postIds = likeServiceFeignClient.getTopPostIdsByCount(count).getBody();
        return postRepository.findAllByIdIn(postIds, pageable);
    }

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
        commentService.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
        likeServiceFeignClient.deleteByPostId(postId);
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
    public Optional<Post> findByIdAndUserId(Long postId, Long userId) {
        return postRepository.findByIdAndUserId(postId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Long findUserIdByPostId(Long postId) {
        return postRepository.findUserIdByPostId(postId);
    }
}
