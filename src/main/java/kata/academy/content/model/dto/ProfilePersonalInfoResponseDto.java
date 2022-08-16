package kata.academy.content.model.dto;

import kata.academy.content.model.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfilePersonalInfoResponseDto(
        Long profileId,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate birthday) {
}
