package ru.itis.websockets.repository;

import ru.itis.websockets.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByLogin(String login);
    void save(User user);
}
