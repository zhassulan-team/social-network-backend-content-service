package kata.academy.eurekacontentservice.outer;

import com.fasterxml.jackson.databind.*;
import kata.academy.eurekacontentservice.*;
import kata.academy.eurekacontentservice.feign.*;
import kata.academy.eurekacontentservice.model.dto.*;
import org.hamcrest.core.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.test.web.servlet.result.*;
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

    @Test
    public void addPost_RequestBodyMissedTest() throws Exception {
        long userId = 1L;
        mockMvc.perform(post("/api/v1/content/posts")
                        .header("userId", Long.toString(userId)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text",
                        Is.is("В запросе не указано тело")));
    }

    @Test
    public void addPost_HeaderMissedTest() throws Exception {
        String title = "title";
        String text = "text";
        String requestJson = String.format("{\"title\":\"%s\",\"text\":\"%s\"}", title, text);

        mockMvc.perform(post("/api/v1/content/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text",
                        Is.is("Required request header 'userId' for method parameter type Long is not present")));
    }

    @Test
    public void addPost_RequestBodyParamMissedTest() throws Exception {
        long userId = 1L;
        mockMvc.perform(post("/api/v1/content/posts")
                        .header("userId", Long.toString(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"title\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text",
                        Is.is("В теле запроса допущены ошибки в следующих полях: [text]")));
    }


    //TODO /api/v1/content/posts

    // / POST addPost (PostRequestDto dto, Long userId) (нет тела, нет поля, нет заголовка userId, успешное создание)

    //todo / GET getPostPage (List<String> tags, Pageable pageable)
    // /owner GET getPostPageByOwner (List<String> tags, Long userId, Pageable pageable)
    // /top GET getPostPageByTop(Integer count, Pageable pageable)
    // /{postId} PUT updatePost(PostRequestDto dto, Long postId, Long userId)
    // /{postId} DELETE deletePost(Long postId, Long userId)
}
