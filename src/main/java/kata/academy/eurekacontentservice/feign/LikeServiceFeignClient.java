package kata.academy.eurekacontentservice.feign;

import kata.academy.eurekacontentservice.feign.fallback.LikeServiceFallbackFactory;
import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import java.util.List;

@FeignClient(value = "eureka-like-service", fallbackFactory = LikeServiceFallbackFactory.class)
public interface LikeServiceFeignClient {

    @GetMapping("/api/internal/v1/likes/posts/top")
    List<Long> getTopPostIdsByCount(@RequestParam @Positive Integer count);

    @DeleteMapping("/api/internal/v1/likes/posts/{postId}")
    void deleteByPostId(@PathVariable @Positive Long postId);

    @DeleteMapping("/api/internal/v1/likes/comments/{commentId}")
    void deleteByCommentId(@PathVariable @Positive Long commentId);

    @DeleteMapping("/api/internal/v1/likes/comments")
    void deleteAllByCommentIds(@RequestBody List<Long> commentIds);

    @GetMapping("/api/internal/v1/likes/posts")
    List<PostLikeResponseDto> getPostLikeResponseDtoByPostId(@RequestParam List<Long> postIds);
}
