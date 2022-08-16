package kata.academy.content.dao.impl.dto;

import kata.academy.content.dao.dto.ProfileSearchedInfoResponseDtoDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfileSearchedInfoResponseDtoDaoImpl implements ProfileSearchedInfoResponseDtoDao {

    @PersistenceContext
    private EntityManager entityManager;
}
