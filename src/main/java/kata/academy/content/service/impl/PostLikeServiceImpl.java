package kata.academy.content.service.impl;

import kata.academy.content.dao.PostLikeDao;
import kata.academy.content.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeDao postLikeDao;
}
