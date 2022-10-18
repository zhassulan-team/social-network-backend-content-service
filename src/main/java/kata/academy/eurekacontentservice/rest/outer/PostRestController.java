package kata.academy.eurekacontentservice.rest.outer;

import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.PostRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping("/api/v1/content/posts")
public class PostRestController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<Post>> getPostPage(@RequestParam(required = false) List<String> tags, Pageable pageable) {
        return ResponseEntity.ok(postService.findAllByTags(tags, pageable));
    }

    @GetMapping("/owner")
    public ResponseEntity<Page<Post>> getPostPageByOwner(@RequestParam(required = false) List<String> tags,
                                                         @RequestHeader @Positive Long userId,
                                                         Pageable pageable) {
        return ResponseEntity.ok(postService.findAllByUserIdAndTags(userId, tags, pageable));
    }

    @GetMapping("/top")
    public ResponseEntity<Page<Post>> getPostPageByTop(@RequestParam(defaultValue = "100") @Positive Integer count, Pageable pageable) {
        return ResponseEntity.ok(postService.findAllTopByCount(count, pageable));
    }

    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody @Valid PostRequestDto dto,
                                        @RequestHeader @Positive Long userId) {
        Post post = PostMapper.toEntity(dto);
        post.setUserId(userId);
        return ResponseEntity.ok(postService.addPost(post));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@RequestBody @Valid PostRequestDto dto,
                                           @PathVariable @Positive Long postId,
                                           @RequestHeader @Positive Long userId) {
        Optional<Post> optionalPost = postService.findByIdAndUserId(postId, userId);
        ApiValidationUtil.requireTrue(optionalPost.isPresent(),
                String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        return ResponseEntity.ok(postService.updatePost(PostMapper.toEntity(dto, optionalPost.get())));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable @Positive Long postId,
                                           @RequestHeader @Positive Long userId) {
        ApiValidationUtil.requireTrue(postService.existsByIdAndUserId(postId, userId),
                String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        postService.deleteById(postId);
        return ResponseEntity.ok().build();
    }
}
