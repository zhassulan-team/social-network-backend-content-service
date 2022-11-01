package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
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
@Transactional
@Service
public class PostResponseDtoServiceImpl implements PostResponseDtoService {

    private final PostRepository postRepository;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAllByTags(List<String> tags, Pageable pageable) {
        Page<Post> postPage;
        if (tags == null || tags.isEmpty()) {
            postPage =  postRepository.findAll(pageable);
        } else {
            postPage = postRepository.findAllDistinctByTagsIn(tags, pageable);
        }
        return convertPageToPagePostResponseDto(postPage);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable) {
        Page<Post> postPage;
        if (tags == null || tags.isEmpty()) {
            postPage =  postRepository.findAllByUserId(userId, pageable);
        } else {
            postPage = postRepository.findAllDistinctByUserIdAndTagsIn(userId, tags, pageable);
        }
        return convertPageToPagePostResponseDto(postPage);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAllTopByCount(Integer count, Pageable pageable) {
        List<Long> postIds = likeServiceFeignClient.getTopPostIdsByCount(count).getBody();
        return convertPageToPagePostResponseDto(postRepository.findAllByIdIn(postIds, pageable));
    }

    @Override
    public PostResponseDto addPost(Post post) {
        return PostMapper.toDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto updatePost(Post post) {
        return PostMapper.toDto(postRepository.save(post));
    }

    private Page<PostResponseDto> convertPageToPagePostResponseDto(Page<Post> postPage) {
        List<PostLikeResponseDto> postLikeResponseDtoList;
        postLikeResponseDtoList = likeServiceFeignClient
                .getLikesByPostsIds(postPage.get().map(Post::getId).toList())
                .getBody();
        return postPage.map(post -> PostMapper.toDtoWithListPostLikeDto(post, postLikeResponseDtoList));
    }
}
