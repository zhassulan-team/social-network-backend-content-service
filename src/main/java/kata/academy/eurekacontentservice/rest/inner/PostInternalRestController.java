package kata.academy.eurekacontentservice.rest.inner;

import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/internal/v1/posts")
public class PostInternalRestController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/{postId}/exists")
    public ResponseEntity<Boolean> existsByPostId(@PathVariable @Positive Long postId) {
        return ResponseEntity.ok(postService.existsById(postId));
    }

    @GetMapping("{postId}/comments")
    public List<Long> findCommentIds(@PathVariable @Positive Long postId){
        return commentService.findAllIdsByPostId(postId);
    }
}
