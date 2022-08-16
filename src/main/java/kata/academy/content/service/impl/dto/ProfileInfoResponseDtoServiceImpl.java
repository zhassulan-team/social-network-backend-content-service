package kata.academy.content.service.impl.dto;

import kata.academy.content.dao.dto.ProfileInfoResponseDtoDao;
import kata.academy.content.service.dto.ProfileInfoResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileInfoResponseDtoServiceImpl implements ProfileInfoResponseDtoService {

    private final ProfileInfoResponseDtoDao profileInfoResponseDtoDao;
}
