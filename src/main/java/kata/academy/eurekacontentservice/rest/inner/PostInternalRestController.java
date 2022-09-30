package kata.academy.eurekacontentservice.rest.inner;

import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/internal/v1/posts")
public class PostInternalRestController {

    private final PostService postService;

    @GetMapping("/{postId}/exists")
    public ResponseEntity<Boolean> existsByPostId(@PathVariable @Positive Long postId) {
        return ResponseEntity.ok(postService.existsById(postId));
    }

    @GetMapping("/{postId}/")
    public ResponseEntity<Long> getUserIdByPostId(@PathVariable @Positive Long postId,
                                                  @RequestParam @Positive Long userId) {
        return ResponseEntity.ok(postService.getUserIdByPostId(postId));
    }
}
