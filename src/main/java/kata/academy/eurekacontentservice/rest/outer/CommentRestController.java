package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.eurekacontentservice.model.converter.CommentMapper;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.CommentResponseDtoService;
import kata.academy.eurekacontentservice.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Optional;

@Tag(name = "Comment Rest Controller", description = "CRUD операции с комментариями")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/content/posts")
public class CommentRestController {

    private final CommentService commentService;
    private final PostService postService;
    private final CommentResponseDtoService commentResponseDtoService;

    @ApiOperation(value = "addComment", notes = "Добавление комментария")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное добавление комментария"),
            @ApiResponse(code = 400, message = "Ошибка при вводе параметров для добавления комментария. " +
                    "Проверьте существование поста с указанным postId и пользователя с указанным userId")})
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@RequestParam @NotBlank String text,
                                                         @PathVariable @Positive Long postId,
                                                         @RequestHeader @Positive Long userId) {
        Optional<Post> optionalPost = postService.findById(postId);
        ApiValidationUtil.requireTrue(optionalPost.isPresent(), String.format("Пост с postId %d нет в базе данных", postId));
        return ResponseEntity.ok(CommentMapper.toDto(commentService.addComment(
                Comment
                        .builder()
                        .text(text)
                        .userId(userId)
                        .post(optionalPost.get())
                        .createdDate(LocalDateTime.now())
                        .build()
        )));
    }

    @ApiOperation(value = "updateComment", notes = "Обновление комментария")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное обновление комментария"),
            @ApiResponse(code = 400, message = "Комментария с указанным commentId, оставленного пользователем с указанным userId, " +
                    "относящегося к посту с указанным postId, нет в базе данных")})
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestParam @NotBlank String text,
                                                            @PathVariable @Positive Long postId,
                                                            @PathVariable @Positive Long commentId,
                                                            @RequestHeader @Positive Long userId) {
        Optional<Comment> commentOptional = commentService.findByIdAndPostIdAndUserId(userId, postId, commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        Comment comment = commentOptional.get();
        comment.setText(text);
        return ResponseEntity.ok(CommentMapper.toDto(commentService.updateComment(comment)));
    }

    @ApiOperation(value = "deleteComment", notes = "Удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное удаление комментария"),
            @ApiResponse(code = 400, message = "Комментария с указанным commentId, оставленного пользователем с указанным userId, " +
                    "относящегося к посту с указанным postId, нет в базе данных")})
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable @Positive Long postId,
                                              @PathVariable @Positive Long commentId,
                                              @RequestHeader @Positive Long userId) {
        ApiValidationUtil.requireTrue(commentService.existsByIdAndPostIdAndUserId(commentId, postId, userId), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        commentService.deleteById(commentId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "getCommentPage", notes = "Получение комментариев к посту")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение комментариев к посту"),
            @ApiResponse(code = 400, message = "Поста с указанным postId нет в базе данных")})
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getCommentPage(@PathVariable @Positive Long postId, Pageable pageable) {
        ApiValidationUtil.requireTrue(postService.existsById(postId), String.format("Пост с postId %d нет в базе данных", postId));
        return ResponseEntity.ok(commentResponseDtoService.findAllByPostId(postId, pageable));
    }
}
