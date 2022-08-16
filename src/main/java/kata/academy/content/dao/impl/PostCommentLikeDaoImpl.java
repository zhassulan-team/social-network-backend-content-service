package kata.academy.content.dao.impl;

import kata.academy.content.dao.PostCommentLikeDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostCommentLikeDaoImpl implements PostCommentLikeDao {

    @PersistenceContext
    private EntityManager entityManager;
}
