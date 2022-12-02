package kata.academy.eurekacontentservice.rest.inner;

import kata.academy.eurekacontentservice.SpringSimpleContextTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostInternalRestControllerTestIT extends SpringSimpleContextTest {

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/inner/PostInternalRestController/existsByCommentId_SuccessfulTest/BeforeTest.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/inner/PostInternalRestController/existsByCommentId_SuccessfulTest/AfterTest.sql")
    public void existsByPostId_SuccessfulTest() throws Exception {
        Long postId = 1L;
        mockMvc.perform(get("/api/internal/v1/content/posts/{postId}/exists", postId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }

    @Test
    public void existsByPostId_FailTest() throws Exception {
        Long postId =1L;
        mockMvc.perform(get("/api/internal/v1/content/posts/{postId}/exists", postId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect((content().string("false")));
    }
}