package kata.academy.eurekacontentservice.feign.fallback;

import kata.academy.eurekacontentservice.exception.FeignRequestException;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

record LikeServiceFallback(Throwable cause) implements LikeServiceFeignClient {

    @Override
    public ResponseEntity<List<Long>> getTopPostIdsByCount(Integer count) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public ResponseEntity<Void> deleteByPostId(Long postId) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public ResponseEntity<Void> deleteByCommentId(Long commentId) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public ResponseEntity<Void> deleteAllByCommentIds(List<Long> commentIds) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public ResponseEntity<List<PostLikeResponseDto>> getLikesByPostsIds(List<Long> postIds) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }
}
