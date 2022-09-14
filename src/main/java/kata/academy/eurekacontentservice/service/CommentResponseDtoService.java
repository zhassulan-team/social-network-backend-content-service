package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentResponseDtoService {

    Page<CommentResponseDto> findByIdAndPostIdAndUserId(Long postId, Long userId, Pageable pageable);

    Page<CommentResponseDto> findByIdAndUserId(Long userId, Pageable pageable);

}
