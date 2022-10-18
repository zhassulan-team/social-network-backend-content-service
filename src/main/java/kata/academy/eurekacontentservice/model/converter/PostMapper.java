package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.PostRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;

import java.time.LocalDateTime;

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
}
