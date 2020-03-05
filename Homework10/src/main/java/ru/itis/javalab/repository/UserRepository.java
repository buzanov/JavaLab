package ru.itis.javalab.repository;

import org.springframework.stereotype.Component;
import ru.itis.javalab.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    void confirmUser(Long id);

    boolean isConfirmed(Long id);

    boolean isConfirmed(String email);

    Optional<User> findUserByEmail(String email);
}
