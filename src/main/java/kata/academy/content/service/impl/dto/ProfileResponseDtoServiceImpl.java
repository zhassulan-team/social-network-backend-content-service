package kata.academy.content.service.impl.dto;

import kata.academy.content.dao.dto.ProfileResponseDtoDao;
import kata.academy.content.service.dto.ProfileResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileResponseDtoServiceImpl implements ProfileResponseDtoService {

    private final ProfileResponseDtoDao profileResponseDtoDao;
}
