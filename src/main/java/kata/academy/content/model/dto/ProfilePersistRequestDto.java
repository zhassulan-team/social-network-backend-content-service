package kata.academy.content.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record ProfilePersistRequestDto(
        @NotNull @Positive Long accountId,
        @NotBlank String username,
        @NotNull String name) {
}
