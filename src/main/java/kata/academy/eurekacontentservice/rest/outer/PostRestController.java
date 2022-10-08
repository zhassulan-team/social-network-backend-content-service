package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Post", description = "The Post API")
public class PostRestController {

    private final PostService postService;

    @Operation(summary = "Get page with posts using tags",
            parameters = {
                    @Parameter(name = "tags", description = "List of tags", required = true),
                    @Parameter(name = "pageable", description = "Pagination information", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Posts page created successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Request is invalid",
                            content = @Content)
            }
    )
    @GetMapping
    public Response<Page<Post>> getPostPage(@RequestParam(required = false) List<String> tags,
                                            Pageable pageable) {
        return Response.ok(postService.findAllByTags(tags, pageable));
    }

    @Operation(summary = "Get page with owner's posts using tags",
            parameters = {
                    @Parameter(name = "tags", description = "List of tags", required = true),
                    @Parameter(name = "pageable", description = "Pagination information", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Posts page created successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Request is invalid",
                            content = @Content)
            }
    )
    @GetMapping("/owner")
    public Response<Page<Post>> getPostPageByOwner(@RequestParam(required = false) List<String> tags,
                                                   @RequestParam @Positive Long userId, Pageable pageable) {
        return Response.ok(postService.findAllByUserIdAndTags(userId, tags, pageable));
    }

    @Operation(summary = "Get page with top posts",
            parameters = {
                    @Parameter(name = "count", description = "Counter", required = true),
                    @Parameter(name = "pageable", description = "Pagination information", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Posts page created successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Request is invalid",
                            content = @Content)
            }
    )
    @GetMapping("/top")
    public Response<Page<Post>> getPostPageByTop(@RequestParam(defaultValue = "100") @Positive Integer count,
                                                 Pageable pageable) {
        return Response.ok(postService.findAllTopByCount(count, pageable));
    }
    @Operation(summary = "Create a new post",
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PostPersistRequestDto.class))}),
            parameters = {
                    @Parameter(name = "userId", description = "User id", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "New post has been created successfully",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostPersistRequestDto.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Request is invalid",
                            content = @Content)
            }
    )
    @PostMapping
    public Response<Post> addPost(@RequestBody @Valid PostPersistRequestDto dto,
                                  @RequestParam @Positive Long userId) {
        Post post = PostMapper.toEntity(dto);
        post.setUserId(userId);
        return Response.ok(postService.addPost(post));
    }

    @Operation(summary = "Update post",
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PostUpdateRequestDto.class))}),
            parameters = {
                    @Parameter(name = "postId", description = "Post id", required = true),
                    @Parameter(name = "userId", description = "User id", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post has been updated successfully",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostUpdateRequestDto.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Post with postId and userId does not exists",
                            content = @Content)
            }
    )
    @PutMapping("/{postId}")
    public Response<Post> updatePost(@RequestBody @Valid PostUpdateRequestDto dto,
                                     @PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId) {
        Optional<Post> optionalPost = postService.findByIdAndUserId(postId, userId);
        ApiValidationUtil.requireTrue(optionalPost.isPresent(),
                String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        return Response.ok(postService.updatePost(PostMapper.toEntity(dto, optionalPost.get())));
    }

    @Operation(summary = "Delete post",
            parameters = {
                    @Parameter(name = "postId", description = "Post id", required = true),
                    @Parameter(name = "userId", description = "User id", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post has been deleted successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Post with postId and userId does not exists",
                            content = @Content)
            }
    )
    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(postService.existsByIdAndUserId(postId, userId),
                String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        postService.deleteById(postId);
        return Response.ok();
    }
}
