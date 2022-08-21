package kata.academy.eurekacontentservice.rest.outer;

import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.request.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.request.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.dto.response.PostResponseDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.abst.entity.PostService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private PostService service;

    public PostRestController(PostService service) {
        this.service = service;
    }

    @PostMapping
    public Response<PostResponseDto> addPost(PostPersistRequestDto dto,
                                             @PathVariable @Positive Long userId){

        return Response.ok(PostMapper.toDto(service.addPost(PostMapper.toEntity(dto))));
    }

    @PutMapping
    public Response<PostResponseDto> updatePost(PostUpdateRequestDto dto,
                                     @PathVariable @Positive Long userId){
        return Response.ok(PostMapper.toDto(service.updatePost(PostMapper.toEntity(dto))));
    }

    @DeleteMapping
    public Response<Void> deletePost(@PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId){
        service.deletePostById(postId);
        return Response.ok();
    }
}
