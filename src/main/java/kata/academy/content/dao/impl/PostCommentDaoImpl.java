package kata.academy.content.dao.impl;

import kata.academy.content.dao.PostCommentDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostCommentDaoImpl implements PostCommentDao {

    @PersistenceContext
    private EntityManager entityManager;
}
