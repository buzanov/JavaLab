package ru.javalab.homework7.repositories;

import java.util.Optional;

public interface CrudRepository<T> {
    T add(T t);

    Optional<T> find(T t);

    boolean update(T t);

    boolean delete(T t);
}
