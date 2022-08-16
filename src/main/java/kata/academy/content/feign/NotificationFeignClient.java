package kata.academy.content.feign;

import kata.academy.content.feign.fallback.NotificationFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "notification", fallbackFactory = NotificationFallbackFactory.class)
public interface NotificationFeignClient {
}
