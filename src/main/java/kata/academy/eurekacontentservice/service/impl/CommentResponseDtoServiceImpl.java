package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.repository.CommentRepository;
import kata.academy.eurekacontentservice.service.CommentResponseDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentResponseDtoServiceImpl implements CommentResponseDtoService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> findByIdAndPostIdAndUserId(Long postId, Long userId, Pageable pageable) {
        return commentRepository.findByIdAndPostIdAndUserId(postId, userId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> findByIdAndUserId(Long userId, Pageable pageable) {
        return commentRepository.findByIdAndUserId(userId, pageable);
    }
}
