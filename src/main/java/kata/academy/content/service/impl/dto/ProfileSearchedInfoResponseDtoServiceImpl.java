package kata.academy.content.service.impl.dto;

import kata.academy.content.dao.dto.ProfileSearchedInfoResponseDtoDao;
import kata.academy.content.service.dto.ProfileSearchedInfoResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileSearchedInfoResponseDtoServiceImpl implements ProfileSearchedInfoResponseDtoService {

    private final ProfileSearchedInfoResponseDtoDao profileSearchedInfoResponseDtoDao;
}
