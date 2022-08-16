package kata.academy.content.model.dto;

import lombok.Builder;

@Builder
public record ProfileInfoResponseDto(
        Long profileId,
        String username,
        String name,
        String avatarPicUrl,
        Integer postCount,
        Integer followerCount,
        Integer followingCount,
        Boolean isFollowing,
        Boolean isClosed) {
}
