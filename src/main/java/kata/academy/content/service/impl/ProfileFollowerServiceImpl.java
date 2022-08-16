package kata.academy.content.service.impl;

import kata.academy.content.dao.ProfileFollowerDao;
import kata.academy.content.service.ProfileFollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileFollowerServiceImpl implements ProfileFollowerService {

    private final ProfileFollowerDao profileFollowerDao;
}
