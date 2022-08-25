package kata.academy.eurekacontentservice.rest.outer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.CommentMapper;
import kata.academy.eurekacontentservice.model.dto.CommentPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.CommentUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.service.entity.CommentService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@Tag(name = "CommentRestController", description = "CRUD операции над комментариями")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class CommentRestController {

    private final CommentService commentService;

    @Operation(summary = "Создание нового комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новый комментарий успешно создан")
    })
    @PostMapping("/{postId}/comments")
    public Response<Comment> addComment(@RequestBody @Valid CommentPersistRequestDto dto,
                                 @PathVariable @Positive Long postId,
                                 @RequestParam @Positive Long userId) {
        return Response.ok(commentService.addComment(CommentMapper.toEntity(dto), postId, userId));
    }


    @Operation(summary = "Эндпоинт для обновление существующего комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Комментарий не найден")
    })
    @PutMapping("/{postId}/comments/{commentId}")
    public Response<Comment> updateComment(@RequestBody @Valid CommentUpdateRequestDto dto,
                                    @PathVariable @Positive Long postId,
                                    @PathVariable @Positive Long commentId,
                                    @RequestParam @Positive Long userId) {
        Optional<Comment> comment = commentService.findByUserIdAndPostIdAndId(userId, postId, commentId);
        ApiValidationUtil.requireTrue(comment.isPresent(), String.format("Comment by id %d not found", commentId));
        return Response.ok(commentService.updateComment(CommentMapper.toEntity(comment.get(), dto), postId, userId));
    }


    @Operation(summary = "Удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "400", description = "Комментарий не найден")
    })
    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<Void> deleteComment(@PathVariable @Positive Long postId,
                                 @PathVariable @Positive Long commentId,
                                 @RequestParam @Positive Long userId) {
        Optional<Comment> comment = commentService.findByUserIdAndPostIdAndId(userId, postId, commentId);
        ApiValidationUtil.requireTrue(comment.isPresent(), String.format("Comment by id %d not found", commentId));
        commentService.deleteComment(comment.get());
        return Response.ok();
    }
}
