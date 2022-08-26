package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.CommentPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.CommentUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toEntity(CommentPersistRequestDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        return comment;
    }

    public static Comment toEntity(CommentUpdateRequestDto dto, Comment comment) {
        comment.setText(dto.text());
        return comment;
    }
}
