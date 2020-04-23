package ru.itis.websockets.repository;

import ru.itis.websockets.model.Message;

import java.util.List;

public interface MessageRepository {
    List<Message> findAllMessagesByChat(Long id);

    void save(Message message);
}
