package kata.academy.eurekacontentservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Positive;

@FeignClient("eureka-like-service")
public interface LikeServiceFeignClient {

    @GetMapping("/api/internal/v1/likes/{postId}/delete")
    ResponseEntity<Void> deleteAllLikes(@PathVariable @Positive Long postId);

}
