package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;
import kata.academy.eurekacontentservice.model.dto.PostRequestDto;
import kata.academy.eurekacontentservice.model.dto.PostResponseDto;
import kata.academy.eurekacontentservice.model.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public final class PostMapper {

    private PostMapper() {
    }

    public static Post toEntity(PostRequestDto dto) {
        return Post
                .builder()
                .title(dto.title())
                .text(dto.text())
                .tags(dto.tags())
                .createdDate(LocalDateTime.now())
                .build();
    }

    public static Post toEntity(PostRequestDto dto, Post post) {
        post.setTitle(dto.title());
        post.setText(dto.text());
        post.setTags(dto.tags());
        post.setCreatedDate(LocalDateTime.now());
        return post;
    }

    public static PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .text(post.getText())
                .createdDate(post.getCreatedDate())
                .tags(post.getTags())
                .positiveLikesCount(0)
                .negativeLikesCount(0).build();
    }

    public static PostResponseDto toDto(Post post, PostLikeResponseDto postLikeResponseDto) {
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .text(post.getText())
                .createdDate(post.getCreatedDate())
                .tags(post.getTags())
                .positiveLikesCount(postLikeResponseDto.positiveLikesCount())
                .negativeLikesCount(postLikeResponseDto.negativeLikesCount()).build();
    }
    public static PostResponseDto toDtoWithListPostLikeDto(Post post, List<PostLikeResponseDto> postLikeResponseDtos) {
        Long id = post.getId();
        for (PostLikeResponseDto postLikeResponseDto : postLikeResponseDtos) {
            if(id.equals(postLikeResponseDto.postId())) {
                return PostMapper.toDto(post, postLikeResponseDto);
            }
        }
        return PostMapper.toDto(post);
    }
}
