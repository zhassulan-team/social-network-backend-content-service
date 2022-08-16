package kata.academy.content.rest.outer;

import kata.academy.content.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/content/v1/posts")
public class PostRestController {

    private final PostService postService;
}
