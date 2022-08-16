package kata.academy.content.service.impl;

import kata.academy.content.dao.PostDao;
import kata.academy.content.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
}
