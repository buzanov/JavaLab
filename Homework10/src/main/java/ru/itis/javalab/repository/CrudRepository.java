package ru.itis.javalab.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> find(ID id);

    T save(T t);

    void update(T t);

    void delete(ID id);
}
