package kata.academy.content.model.dto;

import lombok.Builder;

@Builder
public record ProfileResponseDto(
        Long profileId,
        String username,
        String name,
        String avatarPicUrl,
        Boolean isFollowing) {
}
