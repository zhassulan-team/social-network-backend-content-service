package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.*;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class PostResponseDtoServiceImpl implements PostResponseDtoService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAllByTags(List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return postRepository.findAll(pageable)
                    .map(this::convertToPostResponseDto);
        }
        return postRepository.findAllDistinctByTagsIn(tags, pageable)
                .map(this::convertToPostResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return postRepository.findAllByUserId(userId, pageable)
                    .map(this::convertToPostResponseDto);
        }
        return postRepository.findAllDistinctByUserIdAndTagsIn(userId, tags, pageable)
                .map(this::convertToPostResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAllTopByCount(Integer count, Pageable pageable) {
        List<Long> postIds = likeServiceFeignClient.getTopPostIdsByCount(count);
        return postRepository.findAllByIdIn(postIds, pageable)
                .map(this::convertToPostResponseDto);
    }


    private PostResponseDto convertToPostResponseDto(Post post) {
        PostLikeResponseDto postLikeResponseDto = likeServiceFeignClient.getPostResponseDtoByPostId(List.of(post.getId())).get(0);

        return new PostResponseDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getText(),
                null,
                post.getCreatedDate(),
                post.getTags(),
                postLikeResponseDto.positiveLikesCount(),
                postLikeResponseDto.negativeLikesCount());
    }
}
