package kata.academy.content.util;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

public final class JpaUtil {

    private JpaUtil() {
    }

    public static <T> Optional<T> getSingleResultOrNull(TypedQuery<T> query) {
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ignore) {
        }
        return Optional.empty();
    }

    public static <T> Optional<T> getSingleResultOrNull(Query query) {
        try {
            return Optional.ofNullable((T) query.getSingleResult());
        } catch (NoResultException ignore) {
        }
        return Optional.empty();
    }
}
