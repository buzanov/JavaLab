package dao;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    T create(T model);

    Optional<T> read(ID id);

    void update(T model);

    void delete(ID id);

}
