package kata.academy.eurekacontentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponseDto {
    Long postId;
    Integer positiveLikesCount;
    Integer negativeLikesCount;
}
