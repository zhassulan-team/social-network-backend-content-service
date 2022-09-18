package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentResponseDtoService {

    Page<CommentResponseDto> findAllByPostId(Long postId, Pageable pageable);
}
