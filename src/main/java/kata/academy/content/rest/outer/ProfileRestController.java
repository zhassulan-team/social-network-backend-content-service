package kata.academy.content.rest.outer;

import kata.academy.content.api.Data;
import kata.academy.content.model.dto.ProfileInfoResponseDto;
import kata.academy.content.model.dto.ProfilePersonalInfoResponseDto;
import kata.academy.content.model.dto.ProfileResponseDto;
import kata.academy.content.model.dto.ProfileSearchedInfoResponseDto;
import kata.academy.content.model.enums.Gender;
import kata.academy.content.model.enums.Visibility;
import kata.academy.content.pagination.Page;
import kata.academy.content.service.ProfileService;
import kata.academy.content.service.dto.ProfileInfoResponseDtoService;
import kata.academy.content.service.dto.ProfilePersonalInfoResponseDtoService;
import kata.academy.content.service.dto.ProfileResponseDtoService;
import kata.academy.content.service.dto.ProfileSearchedInfoResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/content/v1/profiles")
public class ProfileRestController {

    private final ProfileService profileService;
    private final ProfileResponseDtoService profileResponseDtoService;
    private final ProfileInfoResponseDtoService profileInfoResponseDtoService;
    private final ProfilePersonalInfoResponseDtoService profilePersonalInfoResponseDtoService;
    private final ProfileSearchedInfoResponseDtoService profileSearchedInfoResponseDtoService;

    @GetMapping("/personal")
    public ResponseEntity<Data<ProfilePersonalInfoResponseDto>> getProfilePersonalInfoResponseDto(
            @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен вернуть персональную информацию текущего юзера
        //Метод должен собрать дто на уровне Dao https://thorben-janssen.com/dto-projections/ -> DTO projections in JPQL
        //Метод должен находится в ProfilePersonalInfoResponseDtoService::getProfilePersonalInfoResponseDto(Long accountId)
        return null;
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(@RequestParam @NotNull String name,
                                           @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен обновить поле
        //Метод должен находится в ProfileService::updateName(String name, Long accountId)
        return null;
    }

    @PatchMapping("/email")
    public ResponseEntity<Void> updateEmail(@RequestParam(required = false) @Email String email,
                                            @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен обновить поле
        //Поле может быть null
        //Метод должен находится в ProfileService::updateEmail(String email, Long accountId)
        return null;
    }

    @PatchMapping("/phone-number")
    public ResponseEntity<Void> updatePhoneNumber(@RequestParam(required = false) String phoneNumber,
                                                  @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен обновить поле
        //Поле может быть null
        //Метод должен находится в ProfileService::updatePhoneNumber(String phoneNumber, Long accountId)
        return null;
    }

    @PatchMapping("/gender")
    public ResponseEntity<Void> updateGender(@RequestParam(required = false) Gender gender,
                                             @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен обновить поле
        //Поле может быть null
        //Метод должен находится в ProfileService::updateGender(Gender gender, Long accountId)
        return null;
    }

    @PatchMapping("/birthday")
    public ResponseEntity<Void> updateBirthday(@RequestParam(required = false) LocalDate birthday,
                                               @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен обновить поле
        //Поле может быть null
        //Метод должен находится в ProfileService::updateBirthday(LocalDate birthday, Long accountId)
        return null;
    }

    @PatchMapping("/visibility")
    public ResponseEntity<Void> updateVisibility(@RequestParam @NotNull Visibility visibility,
                                                 @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен обновить поле
        //Метод должен находится в ProfileService::updateVisibility(Visibility visibility, Long accountId)
        return null;
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<Data<ProfileInfoResponseDto>> getProfileInfoResponseDto(
            @PathVariable @NotNull @Positive Long profileId,
            @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен вернуть информацию о профиле
        //Если профиль не найден, то выбросить EntityNotFoundException
        //Метод должен собрать дто на уровне Dao при помощи NativeQuery и ResultTransformer https://vladmihalcea.com/hibernate-resulttransformer/
        //isFollowing - если пользователь который отправил запрос подписан на этот профиль, то это поле true иначе false
        //isClosed - если isFollowing = false и у этого профиля visibility = PRIVATE, то это поле true иначе false
        //Метод должен находится в ProfileInfoResponseDtoService::getProfileInfoResponseDto(Long profileId, Long accountId)
        return null;
    }

    @GetMapping("/{profileId}/following/{pageNumber}")
    public ResponseEntity<Data<Page<ProfileResponseDto>>> getProfileFollowingPageProfileResponseDto(
            @PathVariable @NotNull @Positive Integer pageNumber,
            @RequestParam(defaultValue = "20") @NotNull @Positive Integer items,
            @PathVariable @NotNull @Positive Long profileId,
            @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен вернуть подписки профиля с пагинацией
        //Если профиль не найден, то выбросить EntityNotFoundException
        //Если для текущего пользователя профиль закрыт isClosed = true (те у профиля visibility = PRIVATE и пользователь который отправил запрос не подписан на данный профиль),
        //то выбросить  исключение ProfileIsClosedException("Профиль закрыт") и обработать в AdviceRestController
        //Метод должен собрать дто на уровне Dao при помощи NativeQuery и ResultTransformer https://vladmihalcea.com/hibernate-resulttransformer/
        //isFollowing - если пользователь который отправил запрос подписан на этот профиль, то это поле true иначе false
        //count - общее количество элементов
        //Сущности у которых isFollowing = true должны быть вверху списка
        //Метод должен находится в ProfileResponseDtoService::getProfileFollowingPageProfileResponseDto(Long profileId, Long accountId, Map<String, Object> parameters)
        return null;
    }

    @GetMapping("/{profileId}/followers/{pageNumber}")
    public ResponseEntity<Data<Page<ProfileResponseDto>>> getProfileFollowersPageProfileResponseDto(
            @PathVariable @NotNull @Positive Integer pageNumber,
            @RequestParam(defaultValue = "20") @NotNull @Positive Integer items,
            @PathVariable @NotNull @Positive Long profileId,
            @RequestHeader @NotNull @Positive Long accountId) {
        //Метод должен вернуть подписчиков профиля с пагинацией
        //Если профиль не найден, то выбросить EntityNotFoundException
        //Если для текущего пользователя профиль закрыт isClosed = true (те у профиля visibility = PRIVATE и пользователь который отправил запрос не подписан на данный профиль),
        //то выбросить  исключение ProfileIsClosedException("Профиль закрыт") и обработать в AdviceRestController
        //Метод должен собрать дто на уровне Dao при помощи NativeQuery и ResultTransformer https://vladmihalcea.com/hibernate-resulttransformer/
        //isFollowing - если пользователь который отправил запрос подписан на этот профиль, то это поле true иначе false
        //count - общее количество элементов
        //Сущности у которых isFollowing = true должны быть вверху списка
        //Метод должен находится в ProfileResponseDtoService::getProfileFollowersPageProfileResponseDto(Long profileId, Long accountId, Map<String, Object> parameters)
        return null;
    }

    @GetMapping("/{pageNumber}")
    public ResponseEntity<Data<Page<ProfileSearchedInfoResponseDto>>> getPageProfileSearchedInfoResponseDto(
            @PathVariable @NotNull @Positive Integer pageNumber,
            @RequestParam(defaultValue = "20") @NotNull @Positive Integer items,
            @RequestParam @NotBlank String filterPattern) {
        //Метод для поиска профилей с пагинацией
        //filterPattern - параметр для поиска по полям username & name без учета регистра
        //Метод должен собрать дто на уровне Dao при помощи NativeQuery и ResultTransformer https://vladmihalcea.com/hibernate-resulttransformer/
        //count - общее количество элементов
        //Метод должен находится в ProfileSearchedInfoResponseDtoService::getPageProfileSearchedInfoResponseDto(Map<String, Object> parameters)
        return null;
    }
}
