package kata.academy.eurekacontentservice.outer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import kata.academy.eurekacontentservice.SpringSimpleContextTest;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.PostLikeResponseDto;
import kata.academy.eurekacontentservice.model.dto.PostRequestDto;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .when(likeServiceFeignClient).getPostLikeResponseDtoByPostId(List.of(1L));
        doReturn(List.of(new PostLikeResponseDto(2L, 0, 1)))
                .when(likeServiceFeignClient).getPostLikeResponseDtoByPostId(List.of(2L));

        mockMvc.perform(get("/api/v1/content/posts")
                        .param("tags", tags)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("title2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].text").value("text2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].positiveLikesCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].negativeLikesCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].createdDate").value("2022-12-06T16:00:00.000005"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].tags", Is.is(List.of("tag2"))));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/PostRestController/getPostPageByOwner_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController/getPostPageByOwner_SuccessfulTest/After.sql")
    public void getPostPageByOwner_SuccessfulTest() throws Exception {
        String tags = "tag1,tag2";
        long userId = 1;
        Pageable pageable = PageRequest.of(0, 1);

        doReturn(List.of(new PostLikeResponseDto(1L, 2, 0)))
                .when(likeServiceFeignClient).getPostLikeResponseDtoByPostId(List.of(1L));

        mockMvc.perform(get("/api/v1/content/posts/owner")
                        .header("userId", String.valueOf(userId))
                        .param("tags", tags)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].text").value("text1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].positiveLikesCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].negativeLikesCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].createdDate").value("2022-12-06T16:00:00.000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].tags", Is.is(List.of("tag1"))));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/PostRestController/getPostPageByTop_TotalElementsTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController/getPostPageByTop_TotalElementsTest/After.sql")
    public void getPostPageByTop_TotalElementsTest() throws Exception {
        int count = 1;

        doReturn(List.of(new PostLikeResponseDto(1L, 1, 0)))
                .when(likeServiceFeignClient).getPostLikeResponseDtoByPostId(List.of(1L));
        doReturn(List.of(new PostLikeResponseDto(2L, 2, 1)))
                .when(likeServiceFeignClient).getPostLikeResponseDtoByPostId(List.of(2L));

        doReturn(List.of(2L)).when(likeServiceFeignClient).getTopPostIdsByCount(count);

        mockMvc.perform(get("/api/v1/content/posts/top")
                        .param("count", String.valueOf(count))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(2));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/PostRestController/updatePost_CorrectTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController/updatePost_CorrectTest/After.sql")
    public void updatePost_CorrectTest() throws Exception {
        long postId = 1;
        long userId = 1;
        String title = "titleNew";
        String text = "textNew";
        String tag = "tagNew";
        List<String> tags = List.of(tag);
        PostRequestDto dto = new PostRequestDto(title, text, tags);

        mockMvc.perform(put("/api/v1/content/posts/{postId}", postId)
                        .header("userId", String.valueOf(userId))
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(entityManager.createQuery("""
                        SELECT p.id
                        FROM Post p
                        WHERE p.userId = :userId
                        AND p.title = :title
                        AND p.text = :text
                        """, Long.class)
                .setParameter("userId", userId)
                .setParameter("title", title)
                .setParameter("text", text)
                .getSingleResult()
                .longValue(), postId);
    }

    @Test
    public void updatePost_NotExistsTest() throws Exception {
        long postId = 1;
        long userId = 1;
        String title = "title1";
        String text = "text1";
        String tag = "tag1";
        List<String> tags = List.of(tag);
        PostRequestDto dto = new PostRequestDto(title, text, tags);

        mockMvc.perform(put("/api/v1/content/posts/{postId}", postId)
                        .header("userId", String.valueOf(userId))
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text")
                        .value(String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId)));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/PostRestController/deletePost_CorrectTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController/deletePost_CorrectTest/After.sql")
    public void deletePost_CorrectTest() throws Exception {
        long postId = 1;
        long userId = 1;

        mockMvc.perform(delete("/api/v1/content/posts/{postId}", postId)
                        .header("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertFalse(entityManager.createQuery("""
                        SELECT COUNT(p.id) > 0
                        FROM Post p
                        WHERE p.userId = :userId
                        """, Boolean.class)
                .setParameter("userId", userId)
                .getSingleResult());
    }

    @Test
    public void deletePost_NotExistsTest() throws Exception {
        long postId = 1;
        long userId = 1;

        mockMvc.perform(delete("/api/v1/content/posts/{postId}", postId)
                        .header("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text")
                        .value(String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId)));
    }
}
