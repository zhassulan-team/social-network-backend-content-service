package kata.academy.eurekacontentservice.service.abst.entity;

import kata.academy.eurekacontentservice.model.entity.Post;


public interface PostService {
    Post addPost(Post post);
    Post updatePost(Post post);
    void deletePostById(Long postId);
    boolean existsPostByIdAndUserId(Long postId, Long userId);


}
