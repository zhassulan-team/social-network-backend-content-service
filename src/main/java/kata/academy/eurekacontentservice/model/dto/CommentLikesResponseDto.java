package kata.academy.eurekacontentservice.model.dto;

import lombok.Builder;

@Builder
public record CommentLikesResponseDto(
        Long commentId,
        Integer positiveLikeCount,
        Integer negativeLikeCount) {
}

