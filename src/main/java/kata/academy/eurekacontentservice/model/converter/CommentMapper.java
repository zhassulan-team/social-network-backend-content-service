package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.model.entity.Comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto
                .builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .text(comment.getText())
                .postId(comment.getPost().getId())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
