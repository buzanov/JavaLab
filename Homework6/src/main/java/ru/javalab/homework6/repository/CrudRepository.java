package ru.javalab.homework6.repository;

import java.util.Optional;

public interface CrudRepository<T> {
    T add(T t);

    Optional<T> find(T t);

    boolean update(T t);

    boolean delete(T t);
}
