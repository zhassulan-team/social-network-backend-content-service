package kata.academy.content.service.impl;

import kata.academy.content.dao.ProfileDao;
import kata.academy.content.model.dto.ProfilePersistRequestDto;
import kata.academy.content.model.entity.Profile;
import kata.academy.content.model.enums.Visibility;
import kata.academy.content.model.mapper.ProfileMapper;
import kata.academy.content.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;

    @Transactional
    @Override
    public void persistProfile(ProfilePersistRequestDto dto) {
        Profile profile = ProfileMapper.toEntity(dto);
        profile.setVisibility(Visibility.PUBLIC);
        profileDao.persistProfile(profile);
    }
}
