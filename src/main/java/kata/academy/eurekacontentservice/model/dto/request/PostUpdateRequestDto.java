package kata.academy.eurekacontentservice.model.dto.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record PostUpdateRequestDto(
        @NotBlank String title,
        @NotBlank String text,
        List<String> tags) {
}
