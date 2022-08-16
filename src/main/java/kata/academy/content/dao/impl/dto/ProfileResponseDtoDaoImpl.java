package kata.academy.content.dao.impl.dto;

import kata.academy.content.dao.dto.ProfileResponseDtoDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfileResponseDtoDaoImpl implements ProfileResponseDtoDao {

    @PersistenceContext
    private EntityManager entityManager;
}
