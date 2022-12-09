package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;
import kata.academy.eurekacontentservice.model.dto.PostResponseDto;
import kata.academy.eurekacontentservice.model.entity.Post;


public final class PostResponseDtoMapper {

    public static PostResponseDto combine(Post post, PostLikeResponseDto postLikeResponseDto) {
        return new PostResponseDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getText(),
                null, // todo post.getPicUrl(),
                post.getCreatedDate(),
                post.getTags(),
                postLikeResponseDto.positiveLikesCount(),
                postLikeResponseDto.negativeLikesCount());
    }
}
