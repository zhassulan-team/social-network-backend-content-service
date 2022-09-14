package kata.academy.eurekacontentservice.rest.outer;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class CommentRestController {

    private final CommentService commentService;
    private final PostService postService;
    private final CommentResponseDtoService commentResponseDtoService;

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

    @PutMapping("/{postId}/comments/{commentId}")
    public Response<CommentResponseDto> updateComment(@RequestBody @Valid CommentUpdateRequestDto dto,
                                                      @PathVariable @Positive Long postId,
                                                      @PathVariable @Positive Long commentId,
                                                      @RequestParam @Positive Long userId) {
        Optional<Comment> commentOptional = commentService.findByIdAndPostIdAndUserId(userId, postId, commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        return Response.ok(CommentMapper.toDto(commentService.updateComment(CommentMapper.toEntity(dto, commentOptional.get()))));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<Void> deleteComment(@PathVariable @Positive Long postId,
                                        @PathVariable @Positive Long commentId,
                                        @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(commentService.existsByIdAndPostIdAndUserId(commentId, postId, userId), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        commentService.deleteById(commentId);
        return Response.ok();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponseDto>> getCommentPage(@PathVariable @Positive Long postId,
                                                             @RequestParam @Positive Long userId,
                                                             Pageable pageable) {
        if (postId != 0) {
            return Response.ok(commentResponseDtoService.findByIdAndPostIdAndUserId(postId, userId, pageable));
        }
        return Response.ok(commentResponseDtoService.findByIdAndUserId(userId, pageable));
    }
}
