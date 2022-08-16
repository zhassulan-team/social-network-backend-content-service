package kata.academy.content.model.mapper;

import kata.academy.content.model.dto.ProfilePersistRequestDto;
import kata.academy.content.model.entity.Profile;

public final class ProfileMapper {

    private ProfileMapper() {
    }

    public static Profile toEntity(ProfilePersistRequestDto dto) {
        return Profile
                .builder()
                .accountId(dto.accountId())
                .username(dto.username())
                .name(dto.name())
                .build();
    }
}
