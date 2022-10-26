package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.CommentLikesResponseDto;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.model.entity.Comment;
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
        List<Long> commentIds = commentRepository.findAllIdsByPostId(postId);
        List<CommentLikesResponseDto> commentLikes = likeServiceFeignClient.findCommentLikeByCommentId(commentIds).getBody();
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        List<CommentResponseDto> result = new ArrayList<>();
        for (CommentLikesResponseDto l: commentLikes){
            for (Comment c: comments) {
                if (l.commentId().equals(c.getId())) {
                    result.add(CommentResponseDto.builder()
                            .id(c.getId())
                            .postId(c.getPost().getId())
                            .userId(c.getUserId())
                            .text(c.getText())
                            .createdDate(c.getCreatedDate())
                            .positiveLikesCount(l.positiveLikesCount())
                            .negativeLikesCount(l.negativeLikesCount())
                            .build());
                }
            }
        }
        return new PageImpl<>(result,pageable,result.size());
    }
}
