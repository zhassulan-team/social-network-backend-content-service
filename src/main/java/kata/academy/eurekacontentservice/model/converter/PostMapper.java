package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.request.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.request.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.dto.response.PostResponseDto;
import kata.academy.eurekacontentservice.model.entity.Post;

public final class PostMapper {

    public static Post toEntity(PostPersistRequestDto dto){
        Post post = new Post();
        post.setUserId(dto.userId());
        post.setTitle(dto.title());
        post.setText(dto.text());
        post.setTags(dto.tags());
        return post;
    }

    public static Post toEntity(PostUpdateRequestDto dto){
        Post post = new Post();
        post.setTitle(dto.title());
        post.setText(dto.text());
        post.setTags(dto.tags());
        return post;

    }

    public static PostResponseDto toDto(Post post){
        return new PostResponseDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getText(),
                post.getTags()
        );

    }
}
