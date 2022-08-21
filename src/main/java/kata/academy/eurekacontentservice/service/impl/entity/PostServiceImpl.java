package kata.academy.eurekacontentservice.service.impl.entity;

import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.entity.PostRepository;
import kata.academy.eurekacontentservice.service.abst.entity.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository repository;

    @Override
    public Post addPost(Post post) {
        return repository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return repository.save(post);
    }


    public void deletePostById(Long id) {
        repository.deleteById(id);

    }

    @Override
    public Post findPostByPostId(Long id) {
        return repository.findPostById(id);
    }
}
