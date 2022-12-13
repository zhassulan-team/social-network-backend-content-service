package kata.academy.eurekacontentservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;


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