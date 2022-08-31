package kata.academy.eurekacontentservice.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CommentResponseDto(
        Long id,
        Long userId,
        String text,
        Long postId
) {
}
