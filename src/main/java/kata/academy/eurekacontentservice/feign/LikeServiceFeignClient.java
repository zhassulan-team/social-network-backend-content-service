package kata.academy.eurekacontentservice.feign;

import kata.academy.eurekacontentservice.feign.fallback.LikeServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import java.util.List;

@FeignClient(value = "eureka-like-service", fallbackFactory = LikeServiceFallbackFactory.class)
public interface LikeServiceFeignClient {

    @GetMapping("/api/internal/v1/likes/posts")
    ResponseEntity<List<Long>> getTopPostIdsByCount(@RequestParam @Positive Integer count);

    @DeleteMapping("/api/internal/v1/likes/posts/{postId}")
    ResponseEntity<Void> deleteByPostId(@PathVariable @Positive Long postId);

    @DeleteMapping("/api/internal/v1/likes/comments/{commentId}")
    ResponseEntity<Void> deleteByCommentId(@PathVariable @Positive Long commentId);

    @DeleteMapping("/api/internal/v1/likes/comments")
    ResponseEntity<Void> deleteAllByCommentIds(@RequestBody List<Long> commentIds);
}
