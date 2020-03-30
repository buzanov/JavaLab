package ru.itis.javalab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.itis.javalab.model.User;

import java.util.Optional;


@Repository
@Component
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);

}
