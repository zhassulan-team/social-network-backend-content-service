package kata.academy.eurekacontentservice.service.impl.entity;

import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.entity.PostRepository;
import kata.academy.eurekacontentservice.service.abst.entity.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;

    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long postId) {postRepository.deleteById(postId);}

    public boolean existsPostByIdAndUserId(Long postId, Long userId){
        return postRepository.existsPostByIdAndUserId(postId, userId);
    }

}
