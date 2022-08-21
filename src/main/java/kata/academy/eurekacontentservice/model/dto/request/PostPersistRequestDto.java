package kata.academy.eurekacontentservice.model.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record PostPersistRequestDto(
        @NotNull Long userId,
        @NotBlank String title,
        @NotBlank String text,
        List<String> tags) {
}
