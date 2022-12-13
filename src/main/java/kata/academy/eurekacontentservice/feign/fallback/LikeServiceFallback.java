package kata.academy.eurekacontentservice.feign.fallback;

import kata.academy.eurekacontentservice.exception.FeignRequestException;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;

import java.util.List;

record LikeServiceFallback(Throwable cause) implements LikeServiceFeignClient {

    @Override
    public List<Long> getTopPostIdsByCount(Integer count) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public void deleteByPostId(Long postId) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public void deleteByCommentId(Long commentId) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public void deleteAllByCommentIds(List<Long> commentIds) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public List<PostLikeResponseDto> getPostLikeResponseDtoByPostId(List<Long> postIds) {
        throw new FeignRequestException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }
}
