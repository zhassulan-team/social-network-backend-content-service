package kata.academy.content.dao.impl.dto;

import kata.academy.content.dao.dto.ProfileInfoResponseDtoDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfileInfoResponseDtoDaoImpl implements ProfileInfoResponseDtoDao {

    @PersistenceContext
    private EntityManager entityManager;
}
