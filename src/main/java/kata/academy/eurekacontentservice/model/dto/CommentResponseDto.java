package kata.academy.eurekacontentservice.model.dto;

public record CommentResponseDto(
        Long id,
        Long userId,
        String text,
        Long postId) {
}
