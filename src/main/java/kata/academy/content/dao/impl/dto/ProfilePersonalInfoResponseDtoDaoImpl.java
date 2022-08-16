package kata.academy.content.dao.impl.dto;

import kata.academy.content.dao.dto.ProfilePersonalInfoResponseDtoDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfilePersonalInfoResponseDtoDaoImpl implements ProfilePersonalInfoResponseDtoDao {

    @PersistenceContext
    private EntityManager entityManager;
}
