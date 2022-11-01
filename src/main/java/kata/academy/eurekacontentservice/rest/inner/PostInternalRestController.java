package kata.academy.eurekacontentservice.rest.inner;

import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/internal/v1/content/posts")
public class PostInternalRestController {

    private final PostService postService;

    @GetMapping("/{postId}/exists")
    public Boolean existsByPostId(@PathVariable @Positive Long postId) {
        return postService.existsById(postId);
    }

    @GetMapping("/{postId}/user-id")
    public Long getUserIdByPostId(@PathVariable @Positive Long postId) {
        return postService.findUserIdByPostId(postId);
    }
}
