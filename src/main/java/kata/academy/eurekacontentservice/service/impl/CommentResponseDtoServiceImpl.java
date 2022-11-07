package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.CommentLikesResponseDto;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.repository.CommentRepository;
import kata.academy.eurekacontentservice.service.CommentResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class CommentResponseDtoServiceImpl implements CommentResponseDtoService {

    private final CommentRepository commentRepository;

    private final LikeServiceFeignClient likeServiceFeignClient;

    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> findAllByPostId(Long postId, Pageable pageable) {
        List<CommentResponseDto> result =new ArrayList<>();
        Page<CommentResponseDto> comments = commentRepository.findAllByPostId(postId, pageable);
        List<CommentLikesResponseDto> commentLikes = likeServiceFeignClient
                .getCommentLikesByCommentId(comments.get().map(CommentResponseDto::id).toList())
                .getBody();
        for (CommentResponseDto comment: comments) {
            for (CommentLikesResponseDto like: commentLikes) {
                if (comment.id().equals(like.commentId())) {
                    result.add(CommentResponseDto.builder()
                            .id(comment.id())
                            .postId(comment.postId())
                            .userId(comment.userId())
                            .text(comment.text())
                            .createdDate(comment.createdDate())
                            .positiveLikesCount(like.positiveLikeCount())
                            .negativeLikesCount(like.negativeLikeCount())
                            .build());
                }
            }
        }
        return new PageImpl<>(result,pageable, result.size());
    }
}

