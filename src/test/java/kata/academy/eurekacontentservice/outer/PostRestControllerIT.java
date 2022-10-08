package kata.academy.eurekacontentservice.outer;

import kata.academy.eurekacontentservice.SpringSimpleContextTest;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.PostPersistRequestDto;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

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
    LikeServiceFeignClient likeServiceFeignClient;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/After.sql")
    public void getPostPage_SuccessfulTest() throws Exception {

        List<String> tags = new ArrayList<>();
        tags.add("s2");

        var count = entityManager
                .createNativeQuery(
                        "SELECT DISTINCT count(p.id) " +
                                "FROM posts p " +
                                "JOIN posts_tags pt " +
                                "ON pt.tag IN (:tags) " +
                                "AND pt.post_id = p.id"
                )
                .setParameter("tags", tags)
                .getSingleResult().toString();

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)));
        Assertions.assertEquals("2", count);

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/PostRestController2/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/PostRestController2/After.sql")
    public void getPostPageByOwner_SuccessfulTest() throws Exception {

        Long userId = 1L;

        List<String> tags = new ArrayList<>();
        tags.add("s2");

        var count = entityManager
                .createNativeQuery(
                        "SELECT DISTINCT count(p.id) " +
                                "FROM posts p " +
                                "JOIN posts_tags pt " +
                                "ON pt.tag IN (:tags) " +
                                "AND pt.post_id = p.id " +
                                "AND p.user_id = :userId"
                )
                .setParameter("tags", tags)
                .setParameter("userId", userId)
                .getSingleResult().toString();
        mockMvc.perform(get("/api/v1/posts/owner")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)));
        Assertions.assertEquals("1", count);

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/After.sql")
    public void getPostPageByTop_SuccessfulTest() throws Exception {
        List<Long> posts = new ArrayList<>();
        posts.add(1L);
        posts.add(2L);
        doReturn(ResponseEntity.ok(posts)).when(likeServiceFeignClient).getTopPostIdsByCount(2);
        mockMvc.perform(get("/api/v1/posts/top")
                        .param("count", String.valueOf(2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/After.sql")
    public void addPost_SuccessfulTest() throws Exception {
        Long userId = 1L;
        PostPersistRequestDto dto = new PostPersistRequestDto("null", "null", null);
        mockMvc.perform(post("/api/v1/posts/")
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId", Is.is(userId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text", Is.is(dto.text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title", Is.is(dto.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tags", Is.is(dto.tags())));
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(po.id) > 0
                                FROM Post po
                                WHERE po.userId = :userId
                                AND po.text = :text
                                AND po.title = :title
                                """, Boolean.class)
                .setParameter("userId", userId)
                .setParameter("text", dto.text())
                .setParameter("title", dto.title())
                .getSingleResult());

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/updatePost_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/After.sql")
    public void updatePost_SuccessfulTest() throws Exception {
        Long userId = 1L;
        Long postId = 1L;
        PostPersistRequestDto dto = new PostPersistRequestDto("null", "null", null);
        mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId", Is.is(userId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text", Is.is(dto.text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title", Is.is(dto.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tags", Is.is(dto.tags())));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/deletePost_SucсessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            value = "/scripts/outer/PostRestController2/After.sql")
    public void deletePost_SuccessfulTest() throws Exception {
        Long userId = 1L;
        Long postId = 1L;
        doReturn(ResponseEntity.ok(Void.TYPE)).when(likeServiceFeignClient).deleteByPostId(postId);
        PostPersistRequestDto dto = new PostPersistRequestDto("null", "null", null);
        mockMvc.perform(delete("/api/v1/posts/{postId}", postId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)));
    }
}



