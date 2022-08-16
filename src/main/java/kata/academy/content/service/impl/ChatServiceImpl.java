package kata.academy.content.service.impl;

import kata.academy.content.dao.ChatDao;
import kata.academy.content.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatDao chatDao;
}
