package kata.academy.eurekacontentservice.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDto(
        Long id,
        Long userId,
        String text,
        Long postId,
        LocalDateTime createdDate,
        Integer positiveLikesCount,
        Integer negativeLikesCount) {
}
