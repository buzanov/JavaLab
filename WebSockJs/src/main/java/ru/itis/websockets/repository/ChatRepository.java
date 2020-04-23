package ru.itis.websockets.repository;

import ru.itis.websockets.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> findChatById(Long id);

    void save(Chat chat);

    List<Chat> findAllChats();
}
