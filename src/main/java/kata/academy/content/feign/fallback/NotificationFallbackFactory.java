package kata.academy.content.feign.fallback;

import kata.academy.content.feign.NotificationFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationFallbackFactory implements FallbackFactory<NotificationFeignClient> {

    @Override
    public NotificationFeignClient create(Throwable cause) {
        return new NotificationFallback(cause);
    }
}
