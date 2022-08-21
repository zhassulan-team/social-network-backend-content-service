package kata.academy.eurekacontentservice.repository.entity;


import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("""
            SELECT p
            FROM Post p JOIN FETCH p.tags
            WHERE p.id = :id 
            """)
    Post findPostById(@Param("id") Long id);

}
