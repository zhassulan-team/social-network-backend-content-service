package kata.academy.eurekacontentservice.repository.entity;


import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("""
            SELECT p
            FROM Post p JOIN FETCH p.tags
            WHERE p.id = :id 
            """)
    Optional<Post> findPostById(@Param("id") Long id);

    @Query("""
            SELECT p
            FROM Post p JOIN FETCH p.tags
            WHERE p.userId = :userId 
            """)
    Optional<Post> findPostByUserid(@Param("userId") Long userId);



}
