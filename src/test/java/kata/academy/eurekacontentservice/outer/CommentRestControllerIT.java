package kata.academy.eurekacontentservice.outer;


import kata.academy.eurekacontentservice.SpringSimpleContextTest;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.dto.CommentLikesResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        CommentResponseDto commentResponseDto = new CommentResponseDto(1L, 1L, "qwerty", 1L, LocalDateTime.now(), 0, 0);
        mockMvc.perform(post("/api/v1/content/posts/{postId}/comments", commentResponseDto.postId())
                        .header("userId", commentResponseDto.userId())
                        .param("text", commentResponseDto.text())
                        .content(objectMapper.writeValueAsString(commentResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(commentResponseDto.id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Is.is(commentResponseDto.userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(commentResponseDto.text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId", Is.is(commentResponseDto.postId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.positiveLikesCount", Is.is(commentResponseDto.positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.negativeLikesCount", Is.is(commentResponseDto.negativeLikesCount())));

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
        CommentResponseDto commentResponseDto = new CommentResponseDto(1L, 1L, "qwerty", 1L, LocalDateTime.now(), 0, 0);
        mockMvc.perform(post("/api/v1/content/posts/{postId}/comments", commentResponseDto.postId())
                        .header("userId", commentResponseDto.userId())
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

        CommentResponseDto commentResponseDto = new CommentResponseDto(1L, 1L, "Hello", 1L, LocalDateTime.now(), 0, 0);
        mockMvc.perform(put("/api/v1/content/posts/{postId}/comments/{commentId}", commentResponseDto.postId(), commentResponseDto.id())
                        .header("userId", commentResponseDto.userId())
                        .param("text", commentResponseDto.text())
                        .content(objectMapper.writeValueAsString(commentResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(commentResponseDto.id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Is.is(commentResponseDto.userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(commentResponseDto.text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId", Is.is(commentResponseDto.postId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.positiveLikesCount", Is.is(commentResponseDto.positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.negativeLikesCount", Is.is(commentResponseDto.negativeLikesCount())));
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

        CommentResponseDto commentResponseDto = new CommentResponseDto(1L, 1L, "Hello", 1L, LocalDateTime.now(), 0, 0);
        mockMvc.perform(put("/api/v1/content/posts/{postId}/comments/{commentId}", commentResponseDto.postId(), commentResponseDto.id())
                        .header("userId", commentResponseDto.userId())
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

        List<Long> commentIdSortedByText = new ArrayList<>();
        commentIdSortedByText.add(3L);
        commentIdSortedByText.add(1L);
        commentIdSortedByText.add(2L);
        commentIdSortedByText.add(4L);

        List<Long> commentIdSortedByUserId = new ArrayList<>();
        commentIdSortedByUserId.add(3L);
        commentIdSortedByUserId.add(6L);
        commentIdSortedByUserId.add(5L);

        List<CommentLikesResponseDto> likesSortedByText = new ArrayList<>();
        likesSortedByText.add(new CommentLikesResponseDto(1L, 4, 3));
        likesSortedByText.add(new CommentLikesResponseDto(2L, 2, 8));
        likesSortedByText.add(new CommentLikesResponseDto(3L, 1, 5));
        likesSortedByText.add(new CommentLikesResponseDto(4L, 0, 7));

        List<CommentLikesResponseDto> likesSortedByUserId = new ArrayList<>();
        likesSortedByUserId.add(new CommentLikesResponseDto(3L, 1, 5));
        likesSortedByUserId.add(new CommentLikesResponseDto(6L, 6, 3));
        likesSortedByUserId.add(new CommentLikesResponseDto(5L, 3, 0));

        CommentResponseDto[] sortedByText = {
                new CommentResponseDto(3L, 3L, "All Right!That’s the word on the street, isn’t it?", postId, LocalDateTime.now(), 1, 5),
                new CommentResponseDto(1L, 3L, "Black and white show!", postId, LocalDateTime.now(), 4, 3),
                new CommentResponseDto(2L, 1L, "Could you tell me why you applied for this job?", postId, LocalDateTime.now(), 2, 8),
                new CommentResponseDto(4L, 2L, "Did you feel it?", postId, LocalDateTime.now(), 0, 7)

        };

        CommentResponseDto[] sortedByUserId = {
                new CommentResponseDto(3L, 3L, "All Right!That’s the word on the street, isn’t it?", postId, LocalDateTime.now(), 1, 5),
                new CommentResponseDto(6L, 4L, "Right!", postId, LocalDateTime.now(), 6, 3),
                new CommentResponseDto(5L, 5L, "Terrific!That’s the word on the street, isn’t it?", postId, LocalDateTime.now(), 3, 0)

        };
        doReturn(ResponseEntity.ok(likesSortedByText)).when(likeServiceFeignClient).getCommentLikesByCommentId(commentIdSortedByText);
        mockMvc.perform(get("/api/v1/content/posts/{postId}/comments", postId)
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "text")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id", Is.is(sortedByText[0].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].text", Is.is(sortedByText[0].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].userId", Is.is(sortedByText[0].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].positiveLikesCount", Is.is(sortedByText[0].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].negativeLikesCount", Is.is(sortedByText[0].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id", Is.is(sortedByText[1].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].text", Is.is(sortedByText[1].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].userId", Is.is(sortedByText[1].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].positiveLikesCount", Is.is(sortedByText[1].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].negativeLikesCount", Is.is(sortedByText[1].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].id", Is.is(sortedByText[2].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].text", Is.is(sortedByText[2].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].userId", Is.is(sortedByText[2].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].positiveLikesCount", Is.is(sortedByText[2].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].negativeLikesCount", Is.is(sortedByText[2].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].id", Is.is(sortedByText[3].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].text", Is.is(sortedByText[3].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].userId", Is.is(sortedByText[3].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].positiveLikesCount", Is.is(sortedByText[3].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].negativeLikesCount", Is.is(sortedByText[3].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber", Is.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize", Is.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", Is.is(4)));

        doReturn(ResponseEntity.ok(likesSortedByUserId)).when(likeServiceFeignClient).getCommentLikesByCommentId(commentIdSortedByUserId);
        mockMvc.perform(get("/api/v1/content/posts/{postId}/comments", postId)
                        .param("page", "1")
                        .param("size", "3")
                        .param("sort", "userId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id", Is.is(sortedByUserId[0].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].text", Is.is(sortedByUserId[0].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].userId", Is.is(sortedByUserId[0].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].positiveLikesCount", Is.is(sortedByUserId[0].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].negativeLikesCount", Is.is(sortedByUserId[0].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id", Is.is(sortedByUserId[1].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].text", Is.is(sortedByUserId[1].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].userId", Is.is(sortedByUserId[1].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].positiveLikesCount", Is.is(sortedByUserId[1].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].negativeLikesCount", Is.is(sortedByUserId[1].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].id", Is.is(sortedByUserId[2].id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].postId", Is.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].text", Is.is(sortedByUserId[2].text())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].userId", Is.is(sortedByUserId[2].userId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].positiveLikesCount", Is.is(sortedByUserId[2].positiveLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].negativeLikesCount", Is.is(sortedByUserId[2].negativeLikesCount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted", Is.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", Is.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", Is.is(6)));
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
