package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.request.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.request.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.dto.response.PostResponseDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.abst.entity.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private PostService service;

    public PostRestController(PostService service) {
        this.service = service;
    }


    @Operation(summary = "Создание нового поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новый пост успешно создан")
    })
    @PostMapping
    public Response<PostResponseDto> addPost(@RequestBody @Valid PostPersistRequestDto dto,
                                             @PathVariable @Positive Long userId){
        return Response.ok(PostMapper.toDto(service.addPost(PostMapper.toEntity(dto))));
    }

    @Operation(summary = "Обновление существующего поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Существующий пост успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Пост не найден")
    })
    @PutMapping
    public Response<PostResponseDto> updatePost(@RequestBody @Valid PostUpdateRequestDto dto,
                                     @PathVariable @Positive Long userId){
        Post post = PostMapper.toEntity(dto);
        ApiValidationUtil.requireTrue(post.getId()==userId, "Запрет на редактирование.");
        return Response.ok(PostMapper.toDto(service.updatePost(PostMapper.toEntity(dto))));
    }

    @DeleteMapping
    public Response<Void> deletePost(@PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId){
        Post post = service.findPostByPostId(postId);
        ApiValidationUtil.requireFalse(service.findPostByPostId(postId) != null, "Пост не найден.");
        ApiValidationUtil.requireTrue(post.getUserId()==userId, "Запрет на удаление.");
        service.deletePostById(postId);
        return Response.ok();
    }
}
