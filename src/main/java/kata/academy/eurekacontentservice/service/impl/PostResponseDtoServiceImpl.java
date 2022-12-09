package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.converter.PostResponseDtoMapper;
import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;
import kata.academy.eurekacontentservice.model.dto.PostResponseDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.PostResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostResponseDtoServiceImpl implements PostResponseDtoService {

    private final PostRepository postRepository;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Override
    public Page<PostResponseDto> findAllByTags(List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return convertToPostResponseDtos(postRepository.findAll(pageable));
        }
        return convertToPostResponseDtos(postRepository.findAllDistinctByTagsIn(tags, pageable));
    }

    @Override
    public Page<PostResponseDto> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return convertToPostResponseDtos(postRepository.findAllByUserId(userId, pageable));
        }
        return convertToPostResponseDtos(postRepository.findAllDistinctByUserIdAndTagsIn(userId, tags, pageable));
    }

    @Override
    public Page<PostResponseDto> findAllTopByCount(Integer count, Pageable pageable) {
        List<Long> postIds = likeServiceFeignClient.getTopPostIdsByCount(count);
        return convertToPostResponseDtos(postRepository.findAllByIdIn(postIds, pageable));
    }

    private Page<PostResponseDto> convertToPostResponseDtos(Page<Post> posts) {
        List<Long> ids = posts.map(Post::getId).toList();
        List<PostLikeResponseDto> postLikeResponseDtos = likeServiceFeignClient.getPostLikeResponseDtoByPostId(ids);

        return posts.map(p -> PostResponseDtoMapper.combine(p,
                postLikeResponseDtos.stream().filter(dto -> dto.postId() == p.getId())
                        .findFirst().orElse(new PostLikeResponseDto(p.getId(), 0, 0)))
        );
    }
}
