package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.CommentMapper;
import kata.academy.eurekacontentservice.model.dto.CommentPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.model.dto.CommentUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.CommentResponseDtoService;
import kata.academy.eurekacontentservice.service.CommentService;
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
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Comment", description = "The Comment API")
public class CommentRestController {

    private final CommentService commentService;
    private final PostService postService;
    private final CommentResponseDtoService commentResponseDtoService;

    @Operation(summary = "Create a new comment",
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommentPersistRequestDto.class))}),
            parameters = {
                    @Parameter(name = "postId", description = "Post id", required = true),
                    @Parameter(name = "userId", description = "User id", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "New comment has been created successfully",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentPersistRequestDto.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Post with postId does not exists",
                            content = @Content)
            }
    )
    @PostMapping("/{postId}/comments")
    public Response<CommentResponseDto> addComment(@RequestBody @Valid CommentPersistRequestDto dto,
                                                   @PathVariable @Positive Long postId,
                                                   @RequestParam @Positive Long userId) {
        Optional<Post> optionalPost = postService.findById(postId);
        ApiValidationUtil.requireTrue(optionalPost.isPresent(), String.format("Пост с postId %d нет в базе данных", postId));
        Comment comment = CommentMapper.toEntity(dto);
        comment.setPost(optionalPost.get());
        comment.setUserId(userId);
        return Response.ok(CommentMapper.toDto(commentService.addComment(comment)));
    }

    @Operation(summary = "Update comment",
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommentUpdateRequestDto.class))}),
            parameters = {
                    @Parameter(name = "postId", description = "Post id", required = true),
                    @Parameter(name = "commentId", description = "Comment id", required = true),
                    @Parameter(name = "userId", description = "User id", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment has been updated successfully",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUpdateRequestDto.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Comment with commentId, postId and userId does not exists",
                            content = @Content)
            }
    )
    @PutMapping("/{postId}/comments/{commentId}")
    public Response<CommentResponseDto> updateComment(@RequestBody @Valid CommentUpdateRequestDto dto,
                                                      @PathVariable @Positive Long postId,
                                                      @PathVariable @Positive Long commentId,
                                                      @RequestParam @Positive Long userId) {
        Optional<Comment> commentOptional = commentService.findByIdAndPostIdAndUserId(userId, postId, commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        return Response.ok(CommentMapper.toDto(commentService.updateComment(CommentMapper.toEntity(dto, commentOptional.get()))));
    }

    @Operation(summary = "Delete comment",
            parameters = {
                    @Parameter(name = "postId", description = "Post id", required = true),
                    @Parameter(name = "commentId", description = "Comment id", required = true),
                    @Parameter(name = "userId", description = "User id", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment has been deleted successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Comment with commentId, postId and userId does not exists",
                            content = @Content)
            }
    )
    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<Void> deleteComment(@PathVariable @Positive Long postId,
                                        @PathVariable @Positive Long commentId,
                                        @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(commentService.existsByIdAndPostIdAndUserId(commentId, postId, userId), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        commentService.deleteById(commentId);
        return Response.ok();
    }

    @Operation(summary = "Get page with comments",
            parameters = {
                    @Parameter(name = "postId", description = "Post id", required = true),
                    @Parameter(name = "userId", description = "User id", required = true),
                    @Parameter(name = "pageable", description = "Pagination information", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment page created successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Post with postId does not exists",
                            content = @Content)
            }
    )
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponseDto>> getCommentPage(@PathVariable @Positive Long postId,
                                                             @RequestParam @Positive Long userId,
                                                             Pageable pageable) {
        ApiValidationUtil.requireTrue(postService.existsById(postId), String.format("Пост с postId %d нет в базе данных", postId));
        return Response.ok(commentResponseDtoService.findAllByPostId(postId, pageable));
    }
}
