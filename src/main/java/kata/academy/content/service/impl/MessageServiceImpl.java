package kata.academy.content.service.impl;

import kata.academy.content.dao.MessageDao;
import kata.academy.content.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;
}
