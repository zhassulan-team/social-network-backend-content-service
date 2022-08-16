package kata.academy.content.service;

import kata.academy.content.model.dto.ProfilePersistRequestDto;

public interface ProfileService {

    void persistProfile(ProfilePersistRequestDto dto);
}
