package kata.academy.eurekacontentservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Positive;
import java.util.List;

@FeignClient("eureka-like-service")
public interface LikeServiceFeignClient {

    @DeleteMapping("/api/internal/v1/posts/{postId}/post-likes")
    ResponseEntity<Void> deleteByPostId(@PathVariable @Positive Long postId);

    @GetMapping("/api/internal/v1/posts//top-post-likes")
    ResponseEntity<List<Long>> getPostsByTopLikes(Integer count);
}
