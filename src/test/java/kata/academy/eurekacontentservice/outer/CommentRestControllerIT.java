package kata.academy.eurekacontentservice.outer;

import kata.academy.eurekacontentservice.SpringSimpleContextTest;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.CommentPersistRequestDto;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBeans({
        @MockBean (LikeServiceFeignClient.class)
})
public class CommentRestControllerIT extends SpringSimpleContextTest {

    @Autowired
    private LikeServiceFeignClient likeServiceFeignClient;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/After.sql")
    public void addComment_SuccessfulTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;
        CommentPersistRequestDto dto = new CommentPersistRequestDto("1");
        mockMvc.perform(post("/api/v1/posts/{postId}/comments", postId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId", Is.is(userId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text", Is.is(dto.text())));
        assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(co.id) > 0
                                FROM Comment co
                                WHERE co.userId = :userId
                                AND co.text = :text
                                """, Boolean.class)
                .setParameter("userId", userId)
                .setParameter("text", dto.text())
                .getSingleResult());
    }


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/updateComment_SuccessfulTest/BeforePost.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/updateComment_SuccessfulTest/BeforeComment.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/updateComment_SuccessfulTest/After.sql")
    public void updateComment_SuccessfulTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;
        Long commentId = 1L;
        CommentPersistRequestDto dto = new CommentPersistRequestDto("1");
        mockMvc.perform(put("/api/v1/posts/{postId}/comments/{commentId}", postId, commentId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(commentId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId", Is.is(userId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text", Is.is(dto.text())));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/deleteComment_SuccessfulTest/BeforePost.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/deleteComment_SuccessfulTest/BeforeComment.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/deleteComment_SuccessfulTest/After.sql")
    public void deleteComment_SuccessfulTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;
        Long commentId = 1L;
        doReturn(ResponseEntity.ok(Void.TYPE)).when(likeServiceFeignClient).deleteByCommentId(commentId);
        CommentPersistRequestDto dto = new CommentPersistRequestDto("1");
        mockMvc.perform(delete("/api/v1/posts/{postId}/comments/{commentId}", postId, commentId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/deleteComment_SuccessfulTest/BeforePost.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/deleteComment_SuccessfulTest/After.sql")
    public void deleteComment_NotExistsTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;
        Long commentId = 1L;
        doReturn(ResponseEntity.ok(Void.TYPE)).when(likeServiceFeignClient).deleteByCommentId(commentId);
        CommentPersistRequestDto dto = new CommentPersistRequestDto("1");
        mockMvc.perform(delete("/api/v1/posts/{postId}/comments/{commentId}", postId, commentId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is(
                        String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId))));
    }


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/getComment_SuccessfulTest/BeforePost.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/getComment_SuccessfulTest/BeforeComment.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/getComment_SuccessfulTest/After.sql")
    public void getComment_SuccessfulTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;
        CommentPersistRequestDto dto = new CommentPersistRequestDto("1");
        mockMvc.perform(get("/api/v1/posts/{postId}/comments/", postId)
                        .param("userId", userId.toString())
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(200)));
    }
}

