package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostResponseDtoService {

    Page<PostResponseDto> findAllByTags(List<String> tags, Pageable pageable);

    Page<PostResponseDto> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable);

    Page<PostResponseDto> findAllTopByCount(Integer count, Pageable pageable);
}
