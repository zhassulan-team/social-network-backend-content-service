package kata.academy.content.dao.impl;

import kata.academy.content.dao.MessageDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MessageDaoImpl implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;
}
