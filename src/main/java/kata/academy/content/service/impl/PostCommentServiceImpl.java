package kata.academy.content.service.impl;

import kata.academy.content.dao.PostCommentDao;
import kata.academy.content.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentDao postCommentDao;
}
