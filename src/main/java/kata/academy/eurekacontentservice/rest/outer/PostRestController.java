package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.PostUpdateRequestDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Operation(summary = "Создание нового поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новый пост успешно создан")
    })
    @PostMapping
    public Response<Post> addPost(@RequestBody @Valid PostPersistRequestDto dto,
                                  @RequestParam @Positive Long userId) {
        Post post = PostMapper.toEntity(dto);
        post.setUserId(userId);
        return Response.ok(postService.addPost(post));
    }

    @Operation(summary = "Обновление существующего поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Существующий пост успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Пост не найден")
    })
    @PutMapping("/{postId}")
    public Response<Post> updatePost(@RequestBody @Valid PostUpdateRequestDto dto,
                                     @PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(postService.existsByIdAndUserId(postId, userId), String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        Post post = PostMapper.toEntity(dto);
        post.setId(postId);
        post.setUserId(userId);
        return Response.ok(postService.updatePost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(postService.existsByIdAndUserId(postId, userId), String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        postService.deleteById(postId);
        likeServiceFeignClient.deleteByPostId(postId);
        return Response.ok();
    }

    @GetMapping
    Response<Page<Post>> getPostPage(@RequestBody(required = false) List<String> tags,
                                     Pageable pageable) {
        //Возвращает все посты из базы с пагинацией, если tags передан,
        // то возвращать если хотя бы один тег соответствует посту
        return null;
    }

    @GetMapping("/owner")
    Response<Page<Post>> getPostPageByOwner(@RequestBody(required = false) List<String> tags,
                                            @RequestParam @Positive Long userId,
                                            Pageable pageable) {
        //Возвращает все посты текущего юзера из базы с пагинацией, если tags передан,
        // то возвращать если хотя бы один тег соответствует посту
        return null;
    }

    @GetMapping("/top")
    Response<Page<Post>> getPostPageByTop(@RequestParam(defaultValue = "100") @Positive Integer count,
                                          Pageable pageable) {
        ResponseEntity<List> posts = likeServiceFeignClient.getPostsByTopLikes();
        //Возвращает посты с самым большим количеством лайков из базы с пагинацией
        return null;
    }
}
