package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.dto.*;
import org.springframework.data.domain.*;

import java.util.*;


public interface PostResponseDtoService {

    Page<PostResponseDto> findAllByTags(List<String> tags, Pageable pageable);

    Page<PostResponseDto> findAllByUserIdAndTags(Long userId, List<String> tags, Pageable pageable);

    Page<PostResponseDto> findAllTopByCount(Integer count, Pageable pageable);
}
