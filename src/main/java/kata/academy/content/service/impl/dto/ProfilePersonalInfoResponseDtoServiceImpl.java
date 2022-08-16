package kata.academy.content.service.impl.dto;

import kata.academy.content.dao.dto.ProfilePersonalInfoResponseDtoDao;
import kata.academy.content.service.dto.ProfilePersonalInfoResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfilePersonalInfoResponseDtoServiceImpl implements ProfilePersonalInfoResponseDtoService {

    private final ProfilePersonalInfoResponseDtoDao profilePersonalInfoResponseDtoDao;
}
