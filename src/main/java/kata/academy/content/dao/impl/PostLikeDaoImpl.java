package kata.academy.content.dao.impl;

import kata.academy.content.dao.PostLikeDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostLikeDaoImpl implements PostLikeDao {

    @PersistenceContext
    private EntityManager entityManager;
}
