package kata.academy.eurekacontentservice.repository.dto;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostResponseDtoRepository extends JpaRepository<Post, Long> {
}
