package kata.academy.content.dao.impl;

import kata.academy.content.dao.ProfileDao;
import kata.academy.content.model.entity.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfileDaoImpl implements ProfileDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persistProfile(Profile profile) {
        entityManager.persist(profile);
    }
}
