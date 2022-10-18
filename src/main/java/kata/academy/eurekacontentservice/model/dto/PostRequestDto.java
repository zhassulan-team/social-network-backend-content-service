package kata.academy.eurekacontentservice.model.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record PostRequestDto(
        @NotBlank String title,
        @NotBlank String text,
        List<String> tags) {
}
