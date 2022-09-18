package kata.academy.eurekacontentservice.rest.outer;

import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {
    private final PostService postService;

    @GetMapping
    public Response<Page<Post>> getPostPage(@RequestBody(required = false) List<String> tags,
                                            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.ok(postService.getAllPosts(tags, pageable));
    }

    @GetMapping("/owner")
    public Response<Page<Post>> getPostPageByOwner(@RequestBody(required = false) List<String> tags,
                                                   @RequestParam @Positive Long userId,
                                                   @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.ok(postService.getAllPostsByUserId(tags, userId, pageable));
    }

    @GetMapping("/top")
    public Response<Page<Post>> getPostPageByTop(@RequestParam(defaultValue = "100") @Positive Integer count, Pageable pageable) {
        return Response.ok(postService.getPostsByLikesAmount(count, pageable));
    }

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
