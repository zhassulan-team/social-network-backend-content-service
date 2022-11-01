package kata.academy.eurekacontentservice.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponseDto(
        Long id,
        Long userId,
        String title,
        String text,
        String picUrl,
        LocalDateTime createdDate,
        List<String> tags,
        Integer positiveLikesCount,
        Integer negativeLikesCount) {
}
