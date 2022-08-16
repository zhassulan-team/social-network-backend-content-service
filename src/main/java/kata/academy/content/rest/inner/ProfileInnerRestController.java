package kata.academy.content.rest.inner;

import kata.academy.content.model.dto.ProfilePersistRequestDto;
import kata.academy.content.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/inner/v1/profiles")
public class ProfileInnerRestController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<Void> persistProfile(@RequestBody @Valid ProfilePersistRequestDto dto) {
        profileService.persistProfile(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getProfileUsernamesByProfileIds(@RequestParam @NotNull List<Long> profileIds) {
        //Метод должен вытащить из базы usernames по id профилей
        //Метод должен находится в ProfileService::getProfileUsernamesByProfileIds(List<Long> profileIds)
        return null;
    }
}
