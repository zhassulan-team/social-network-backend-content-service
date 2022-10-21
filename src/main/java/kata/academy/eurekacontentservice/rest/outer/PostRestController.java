package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.PostRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Tag(name = "PostRestControlle", description = "CRUD операции с постами")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/content/posts")
public class PostRestController {

    private final PostService postService;

    @ApiOperation(value = "getPostPag", notes = "Получение страницы с постами по тэгам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение страницы с постами"),
            @ApiResponse(code = 400, message = "Ошибка при получении страницы с постами по указанным тегам")})
    @GetMapping
    public ResponseEntity<Page<Post>> getPostPage(@RequestParam(required = false) List<String> tags, Pageable pageable) {
        return ResponseEntity.ok(postService.findAllByTags(tags, pageable));
    }

    @ApiOperation(value = "getPostPageByOwner", notes = "Получение страницы с постами пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение страницы с постами пользователя"),
            @ApiResponse(code = 400, message = "Ошибка при получении страницы с постами пользователя с указанным userId по указанным тегам")})
    @GetMapping("/owner")
    public ResponseEntity<Page<Post>> getPostPageByOwner(@RequestParam(required = false) List<String> tags,
                                                         @RequestHeader @Positive Long userId,
                                                         Pageable pageable) {
        return ResponseEntity.ok(postService.findAllByUserIdAndTags(userId, tags, pageable));
    }

    @ApiOperation(value = "getPostPageByTop", notes = "Получение топовых постов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение топовых постов"),
            @ApiResponse(code = 400, message = "Ошибка при получении страницы с указанным количеством топовых постов")})
    @GetMapping("/top")
    public ResponseEntity<Page<Post>> getPostPageByTop(@RequestParam(defaultValue = "100") @Positive Integer count, Pageable pageable) {
        return ResponseEntity.ok(postService.findAllTopByCount(count, pageable));
    }

    @ApiOperation(value = "addPost", notes = "Добавление поста")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное добавление поста"),
            @ApiResponse(code = 400, message = "Ошибка при указании параметров для добавления поста")})
    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody @Valid PostRequestDto dto,
                                        @RequestHeader @Positive Long userId) {
        Post post = PostMapper.toEntity(dto);
        post.setUserId(userId);
        return ResponseEntity.ok(postService.addPost(post));
    }

    @ApiOperation(value = "updatePost", notes = "Обновление поста")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное обновление поста"),
            @ApiResponse(code = 400, message = "Ошибка при указании параметров для обновления поста: " +
                    "проверьте существование поста с указанным postId и пользователя с указанным userId, " +
                    "а также корректность заполнения тела запроса при обновлении")})
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@RequestBody @Valid PostRequestDto dto,
                                           @PathVariable @Positive Long postId,
                                           @RequestHeader @Positive Long userId) {
        Optional<Post> optionalPost = postService.findByIdAndUserId(postId, userId);
        ApiValidationUtil.requireTrue(optionalPost.isPresent(),
                String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        return ResponseEntity.ok(postService.updatePost(PostMapper.toEntity(dto, optionalPost.get())));
    }

    @ApiOperation(value = "deletePost", notes = "Удаление поста")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное удаление поста"),
            @ApiResponse(code = 400, message = "Поста с указанным postId, созданного пользователем с указанным userId, " +
                    "нет в базе данных")})
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable @Positive Long postId,
                                           @RequestHeader @Positive Long userId) {
        ApiValidationUtil.requireTrue(postService.existsByIdAndUserId(postId, userId),
                String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        postService.deleteById(postId);
        return ResponseEntity.ok().build();
    }
}
