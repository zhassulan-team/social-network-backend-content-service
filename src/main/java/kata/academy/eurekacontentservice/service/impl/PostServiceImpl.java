package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

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
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndUserId(Long postId, Long userId) {
        return postRepository.existsByIdAndUserId(postId, userId);
    }
}
