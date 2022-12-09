package kata.academy.eurekacontentservice.model.dto;

public record PostLikeResponseDto(
        Long postId,
        Integer positiveLikesCount,
        Integer negativeLikesCount) {
}