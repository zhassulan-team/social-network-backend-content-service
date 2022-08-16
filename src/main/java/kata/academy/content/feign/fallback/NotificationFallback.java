package kata.academy.content.feign.fallback;

import kata.academy.content.feign.NotificationFeignClient;

record NotificationFallback(Throwable cause) implements NotificationFeignClient {
}
