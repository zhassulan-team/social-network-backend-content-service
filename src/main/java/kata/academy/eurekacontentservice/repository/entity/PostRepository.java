package kata.academy.eurekacontentservice.repository.entity;


import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("""
            SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
            FROM Post p
            WHERE p.userId =:userId AND p.id=:postId
            """)
    boolean existsPostByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);




}
