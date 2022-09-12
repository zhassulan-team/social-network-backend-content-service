package kata.academy.eurekacontentservice.rest.outer;

import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;

    @PostMapping
    public Response<Post> addPost(@RequestBody @Valid PostPersistRequestDto dto,
                                  @RequestParam @Positive Long userId) {
        Post post = PostMapper.toEntity(dto);
        post.setUserId(userId);
        return Response.ok(postService.addPost(post));
    }

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
        return Response.ok();
    }
}
