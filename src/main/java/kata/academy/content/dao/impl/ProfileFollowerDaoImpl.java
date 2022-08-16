package kata.academy.content.dao.impl;

import kata.academy.content.dao.ProfileFollowerDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfileFollowerDaoImpl implements ProfileFollowerDao {

    @PersistenceContext
    private EntityManager entityManager;
}
