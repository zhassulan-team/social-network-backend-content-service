package kata.academy.eurekacontentservice.outer;


import kata.academy.eurekacontentservice.SpringSimpleContextTest;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBeans({
        @MockBean(LikeServiceFeignClient.class)
})
public class CommentRestControllerIT extends SpringSimpleContextTest {
    @Autowired
    private LikeServiceFeignClient likeServiceFeignClient;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/After.sql")
    public void addComment_SuccessfulTest() throws Exception {

        CommentResponseDto commentResponseDto = new CommentResponseDto(1L,1L, "qwerty", 1L, LocalDateTime.now());
        mockMvc.perform(post("/api/v1/content/posts/{postId}/comments", commentResponseDto.postId())
                .header("userId",commentResponseDto.userId())
                .param("text", commentResponseDto.text())
                        .content(objectMapper.writeValueAsString(commentResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(commentResponseDto.id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Is.is(commentResponseDto.userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(commentResponseDto.text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId", Is.is(commentResponseDto.postId().intValue())));

        assertTrue(commentResponseDto.createdDate().isBefore(LocalDateTime.now()));
        assertTrue(entityManager.createQuery(
                """
                        SELECT COUNT(cl.id) > 0
                        from Comment cl, Post pl
                        WHERE cl.userId = :userId
                        AND cl.text = :text
                        AND pl.id = :postId
                        """, Boolean.class)
                .setParameter("userId", commentResponseDto.userId())
                .setParameter("text", commentResponseDto.text())
                .setParameter("postId", commentResponseDto.postId())
                .getSingleResult());
    }

    @Test
    public void addComment_PostDoesNotExistTest() throws Exception {
        CommentResponseDto commentResponseDto = new CommentResponseDto(1L,1L, "qwerty", 1L, LocalDateTime.now());
        mockMvc.perform(post("/api/v1/content/posts/{postId}/comments", commentResponseDto.postId())
                        .header("userId",commentResponseDto.userId())
                        .param("text", commentResponseDto.text())
                        .content(objectMapper.writeValueAsString(commentResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(
                        String.format("Пост с postId %d нет в базе данных", commentResponseDto.postId()))));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/updateComment_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/updateComment_SuccessfulTest/After.sql")
    public void updateComment_SuccessfulTest() throws Exception {

        CommentResponseDto commentResponseDto = new CommentResponseDto(1L,1L, "Hello", 1L, LocalDateTime.now());
        mockMvc.perform(put("/api/v1/content/posts/{postId}/comments/{commentId}", commentResponseDto.postId(), commentResponseDto.id())
                        .header("userId",commentResponseDto.userId())
                        .param("text", commentResponseDto.text())
                        .content(objectMapper.writeValueAsString(commentResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(commentResponseDto.id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Is.is(commentResponseDto.userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(commentResponseDto.text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId", Is.is(commentResponseDto.postId().intValue())));

        assertTrue(commentResponseDto.createdDate().isBefore(LocalDateTime.now()));
        assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(cl.id) > 0
                                from Comment cl, Post pl
                                WHERE cl.userId = :userId
                                AND cl.text = :text
                                AND pl.id = :postId
                                """, Boolean.class)
                .setParameter("userId", commentResponseDto.userId())
                .setParameter("text", commentResponseDto.text())
                .setParameter("postId", commentResponseDto.postId())
                .getSingleResult());
    }

    @Test
    public void updateComment_DoesNotExistTest() throws Exception {

        CommentResponseDto commentResponseDto = new CommentResponseDto(1L,1L, "Hello", 1L, LocalDateTime.now());
        mockMvc.perform(put("/api/v1/content/posts/{postId}/comments/{commentId}", commentResponseDto.postId(), commentResponseDto.id())
                        .header("userId",commentResponseDto.userId())
                        .param("text", commentResponseDto.text())
                        .content(objectMapper.writeValueAsString(commentResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(
                        String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных",
                                commentResponseDto.id(), commentResponseDto.postId(), commentResponseDto.userId()))));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/deleteComment_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/deleteComment_SuccessfulTest/After.sql")
    public void deleteComment_SuccessfulTest() throws Exception {

        Long postId = 1L;
        Long commentId = 1L;
        Long userId = 1L;
        doReturn(ResponseEntity.ok(Boolean.TRUE)).when(likeServiceFeignClient).deleteByCommentId(commentId);
        mockMvc.perform(delete("/api/v1/content/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(c.id) = 0
                                FROM Comment c
                                WHERE c.id = :commentId
                                """, Boolean.class)
                .setParameter("commentId", commentId)
                .getSingleResult());
    }

    @Test
    public void deleteComment_DoesNotExistTest() throws Exception {

        Long postId = 1L;
        Long commentId = 1L;
        Long userId = 1L;
        mockMvc.perform(delete("/api/v1/content/posts/{postId}/comments/{commentId}", postId, commentId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(
                        String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId))));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/getCommentPage_SuccessfulTest/Before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/outer/CommentRestController/addComment_SuccessfulTest/getCommentPage_SuccessfulTest/After.sql")
    public void getCommentPage_SuccessfulTest() throws Exception {

        Long postId = 1L;
        mockMvc.perform(get("/api/v1/content/posts/{postId}/comments", postId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].text", Is.is("qwerty")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].userId", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id", Is.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].text", Is.is("Hello")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].userId", Is.is(2)));
    }

    @Test
    public void getCommentPage_DoesNotExistTest() throws Exception {

        Long postId = 1L;
        mockMvc.perform(get("/api/v1/content/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(String.format("Пост с postId %d нет в базе данных", postId))));
    }
}
