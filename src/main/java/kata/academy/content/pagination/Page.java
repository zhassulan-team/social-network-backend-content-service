package kata.academy.content.pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Page<T> {

    private Long count;
    private List<T> entities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page<?> page = (Page<?>) o;
        return Objects.equals(count, page.count) && Objects.equals(entities, page.entities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, entities);
    }
}
