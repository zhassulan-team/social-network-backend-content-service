package kata.academy.eurekacontentservice.model.dto.response;

import java.util.List;

public record PostResponseDto(
        Long id,
        Long userId,
        String title,
        String text,
        List<String> tags) {
}
