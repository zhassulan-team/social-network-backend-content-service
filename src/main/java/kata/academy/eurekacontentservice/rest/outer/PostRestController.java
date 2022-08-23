package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.request.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.request.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.abst.entity.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/posts")

public class PostRestController {

    private final PostService postService;


    @Operation(summary = "Создание нового поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новый пост успешно создан")
    })
    @PostMapping
    public Response<Post> addPost(@RequestBody @Valid PostPersistRequestDto dto,
                                             @RequestParam @Positive Long userId){
        return Response.ok(postService.addPost(PostMapper.toEntity(dto)));
    }

    @Operation(summary = "Обновление существующего поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Существующий пост успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Пост не найден")
    })
    @PutMapping("/{postId}")
    public Response<Post> updatePost(@RequestBody @Valid PostUpdateRequestDto dto,
                                     @RequestParam @Positive Long userId){
        ApiValidationUtil.requireFalse(postService.existsPostByIdAndUserId(PostMapper.toEntity(dto).getId(), userId), "Ошибка запроса");
        return Response.ok(postService.updatePost(PostMapper.toEntity(dto)));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@RequestParam @Positive Long postId,
                                     @RequestParam @Positive Long userId){
        ApiValidationUtil.requireFalse(postService.existsPostByIdAndUserId(postId, userId), "Ошибка запроса");
        postService.deletePostById(postId);
        return Response.ok();
    }
}
