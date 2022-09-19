package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Override
    public Page<Post> getAllPosts(List<String> tags, Pageable pageable) {
        return pickPostIfTagExists(tags, pageable);
    }

    @Override
    public Page<Post> getAllPostsByUserId(List<String> tags, Long userId, Pageable pageable) {
        Page<Post> postsWithTags = pickPostIfTagExists(tags, pageable);
        List<Long> idsByTags = new ArrayList<>();
        Page<Post> posts = null;
        for (Post post : postsWithTags) {
            if (Objects.equals(post.getUserId(), userId)) {
                idsByTags.add(post.getId());
            }
        }
        List<Post> listToReturn = new ArrayList<>();
        for (Long postId : idsByTags) {
            listToReturn.add(postRepository.findById(postId).get());
        }
        return listToPage(pageable, listToReturn);
    }

    private Page<Post> pickPostIfTagExists(List<String> tags, Pageable pageable) {
        if (tags != null && !tags.isEmpty()) {
            List<Post> allPosts = postRepository.findAll();
            Set<Long> postsWithTags = new HashSet<>();
            for (String tag : tags) {
                for (Post post : allPosts) {
                    if (post.getTags().contains(tag)) {
                        postsWithTags.add(post.getId());
                    }
                }
            }
            List<Post> postList = new ArrayList<>();
            for (Long postId : postsWithTags) {
                postList.add(postRepository.findById(postId).get());
            }

            return listToPage(pageable, postList);
        }
        return postRepository.findAll(pageable);
    }

    private static Page<Post> listToPage(Pageable pageable, List<Post> entities) {
        int lowerBound = pageable.getPageNumber() * pageable.getPageSize();
        int upperBound = Math.min(lowerBound + pageable.getPageSize() - 1, entities.size());

        List<Post> subList = entities.subList(lowerBound, upperBound);

        return new PageImpl<Post>(subList, pageable, subList.size());
    }

    @Override
    public Page<Post> getPostsByLikesAmount(Integer count, Pageable pageable) {
        List<Post> posts = new ArrayList<>();
        List<Long> postIds = likeServiceFeignClient.getPostsByLikesAmount(count).getBody();
        for (Long postId : postIds) {
            posts.add(postRepository.findById(postId).get());
        }
        return listToPage(pageable, posts);
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
}
