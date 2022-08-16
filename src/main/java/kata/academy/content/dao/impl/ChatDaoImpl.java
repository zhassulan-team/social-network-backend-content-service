package kata.academy.content.dao.impl;

import kata.academy.content.dao.ChatDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ChatDaoImpl implements ChatDao {

    @PersistenceContext
    private EntityManager entityManager;
}
