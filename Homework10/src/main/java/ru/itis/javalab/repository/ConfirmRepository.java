package ru.itis.javalab.repository;

import org.springframework.stereotype.Component;


public interface ConfirmRepository extends CrudRepository<String, Long> {
    Long findByCode(String string);

    void save(String string, Long aLong);

    void delete(String string);
}
