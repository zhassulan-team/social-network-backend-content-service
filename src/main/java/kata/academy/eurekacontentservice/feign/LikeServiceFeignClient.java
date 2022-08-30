package kata.academy.eurekacontentservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Positive;

@FeignClient("eureka-like-service")
public interface LikeServiceFeignClient {

    @DeleteMapping("/api/internal/v1/posts/{postId}/post-likes")
    ResponseEntity<Void> deleteByPostId(@PathVariable @Positive Long postId);

    @DeleteMapping("api/internal/v1/comments/{commentId}/comment-likes")
    ResponseEntity<Void> deleteByCommentId(@PathVariable @Positive Long commentId);
}
