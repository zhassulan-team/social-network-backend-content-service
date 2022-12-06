package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.*;
import kata.academy.eurekacontentservice.model.dto.*;
import kata.academy.eurekacontentservice.model.entity.*;
import kata.academy.eurekacontentservice.repository.*;
import kata.academy.eurekacontentservice.service.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@RequiredArgsConstructor
@Transactional
@Service
public class PostResponseDtoServiceImpl implements PostResponseDtoService {

    private final PostRepository postRepository;
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
        List<PostLikeResponseDto> postLikeResponseDto = likeServiceFeignClient.getPostResponseDtoByPostId(List.of(post.getId()));
        int positiveLikes = postLikeResponseDto.size() > 0 ? postLikeResponseDto.get(0).positiveLikesCount() : 0;
        int negativeLikes = postLikeResponseDto.size() > 0 ? postLikeResponseDto.get(0).negativeLikesCount() : 0;

        return new PostResponseDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getText(),
                null,
                post.getCreatedDate(),
                post.getTags(),
                positiveLikes,
                negativeLikes
        );
    }
}
