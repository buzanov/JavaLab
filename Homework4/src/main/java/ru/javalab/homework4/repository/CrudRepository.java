package ru.javalab.homework4.repository;

import java.util.Optional;

public interface CrudRepository<T> {
    boolean add(T t);

    Optional<T> find(T t);

    boolean update(T t);

    boolean delete(T t);
}
