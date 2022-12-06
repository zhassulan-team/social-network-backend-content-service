package kata.academy.eurekacontentservice.outer;

import com.fasterxml.jackson.databind.*;
import kata.academy.eurekacontentservice.*;
import kata.academy.eurekacontentservice.feign.*;
import kata.academy.eurekacontentservice.model.dto.*;
import org.hamcrest.core.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.test.web.servlet.result.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
        ObjectWriter ow = mapper.writer();
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

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/PostRestController/getPostPage_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController/getPostPage_SuccessfulTest/After.sql")
    public void getPostPage_SuccessfulTest() throws Exception {
        String tags = "tag1,tag2";
        Pageable pageable = PageRequest.of(1, 1);

        doReturn(List.of(new PostLikeResponseDto(1L, 2, 0)))
                .when(likeServiceFeignClient).getPostResponseDtoByPostId(List.of(1L));
        doReturn(List.of(new PostLikeResponseDto(2L, 0, 1)))
                .when(likeServiceFeignClient).getPostResponseDtoByPostId(List.of(2L));

        mockMvc.perform(get("/api/v1/content/posts")
                        .param("tags", tags)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("title2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].text").value("text2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].positiveLikesCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].negativeLikesCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].createdDate").value("2022-12-06T16:00:00.000005"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].tags", Is.is(List.of("tag2"))))
        ;
    }


    //TODO /api/v1/content/posts

    // / POST addPost (PostRequestDto dto, Long userId) (нет тела, нет поля, нет заголовка userId, успешное создание)
    // / GET getPostPage (List<String> tags, Pageable pageable) (успешное получение 2-й страницы ответа)
    //todo /owner GET getPostPageByOwner (List<String> tags, Long userId, Pageable pageable)
    // /top GET getPostPageByTop(Integer count, Pageable pageable)
    // /{postId} PUT updatePost(PostRequestDto dto, Long postId, Long userId)
    // /{postId} DELETE deletePost(Long postId, Long userId)
}
