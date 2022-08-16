package kata.academy.content.dao.impl;

import kata.academy.content.dao.PostDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostDaoImpl implements PostDao {

    @PersistenceContext
    private EntityManager entityManager;
}
