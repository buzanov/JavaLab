package ru.itis.websockets.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itis.websockets.model.Chat;
import ru.itis.websockets.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class ChatRepositoryImpl implements ChatRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MessageRepository messageRepository;
    public static final String SQL_FIND_CHAT_BY_ID = "SELECT * FROM chat WHERE id = ?";
    public static final String SQL_FIND_ALL_CHATS = "SELECT * FROM chat";

    RowMapper<Chat> mapper = (rs, i) ->
            Chat.builder()
                    .messageList(messageRepository.findAllMessagesByChat(rs.getLong("id")))
                    .id(rs.getLong("id"))
                    .build();

    @Override
    public Optional<Chat> findChatById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CHAT_BY_ID, mapper, id));
    }

    @Override
    public void save(Chat chat) {

    }

    @Override
    public List<Chat> findAllChats() {
        return jdbcTemplate.query(SQL_FIND_ALL_CHATS, mapper);
    }
}
