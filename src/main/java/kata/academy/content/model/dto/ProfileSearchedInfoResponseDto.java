package kata.academy.content.model.dto;

import lombok.Builder;

@Builder
public record ProfileSearchedInfoResponseDto(
        Long profileId,
        String username,
        String name,
        String avatarPicUrl) {
}
