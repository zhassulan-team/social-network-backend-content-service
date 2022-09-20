package kata.academy.eurekacontentservice.rest.inner;

import kata.academy.eurekacontentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/internal/v1/comments")
public class CommentInternalRestController {

    private final CommentService commentService;

    @GetMapping("/{commentId}/exists")
    public ResponseEntity<Boolean> existsByCommentId(@PathVariable @Positive Long commentId) {
        return ResponseEntity.ok(commentService.existsById(commentId));
    }

}
