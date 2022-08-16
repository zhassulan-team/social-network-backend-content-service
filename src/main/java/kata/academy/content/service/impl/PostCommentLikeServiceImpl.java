package kata.academy.content.service.impl;

import kata.academy.content.dao.PostCommentLikeDao;
import kata.academy.content.service.PostCommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostCommentLikeServiceImpl implements PostCommentLikeService {

    private final PostCommentLikeDao postCommentLikeDao;
}
