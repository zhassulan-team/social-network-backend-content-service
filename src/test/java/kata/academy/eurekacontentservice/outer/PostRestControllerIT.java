package kata.academy.eurekacontentservice.outer;

import com.fasterxml.jackson.databind.*;
import kata.academy.eurekacontentservice.*;
import kata.academy.eurekacontentservice.feign.*;
import kata.academy.eurekacontentservice.model.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.util.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
        @MockBean(LikeServiceFeignClient.class)
})
public class PostRestControllerIT extends SpringSimpleContextTest {

    @Autowired
    private LikeServiceFeignClient likeServiceFeignClient;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController/addPost_SuccessfulTest/After.sql")
    public void addPost_SuccessfulTest() throws Exception {
        Long userId = 1L;
        String title = "title";
        String text = "text";
        List<String> tags = List.of("tag1", "tag2");

        PostRequestDto dto = new PostRequestDto(title, text, tags);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/content/posts")
                        .header("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        assertTrue(entityManager.createQuery("""
                        SELECT COUNT(p.id) > 0
                        FROM Post p
                        WHERE p.userId = :userId
                        AND p.title = :title
                        AND p.text = :text
                        """, Boolean.class)
                .setParameter("userId", userId)
                .setParameter("title", title)
                .setParameter("text", text)
                .getSingleResult());
    }

    //todo /api/v1/content/posts
    //todo / getPostPage (List<String> tags, Pageable pageable)
    //todo /owner getPostPageByOwner (List<String> tags, Long userId, Pageable pageable)
    //todo /top getPostPageByTop(Integer count, Pageable pageable)
    //todo /{postId} updatePost(PostRequestDto dto, Long postId, Long userId)
    //todo /{postId} deletePost(Long postId, Long userId)
}
