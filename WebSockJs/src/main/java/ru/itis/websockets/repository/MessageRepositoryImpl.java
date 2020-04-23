package ru.itis.websockets.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itis.websockets.model.Message;
import ru.itis.websockets.model.User;

import java.util.List;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final String SQL_MESSAGES_BY_CHAT_ID = "SELECT * FROM message WHERE chat_id = ?";
    public static final String SQL_SAVE_MESSAGE = "INSERT INTO message(chat_id, text, login) VALUES (?,?,?)";

    RowMapper<Message> mapper = (rs, i) ->
            Message.builder()
                    .chatId(rs.getLong("chat_id"))
                    .login(rs.getString("login"))
                    .text(rs.getString("text"))
                    .build();

    @Override
    public List<Message> findAllMessagesByChat(Long id) {
        return jdbcTemplate.query(SQL_MESSAGES_BY_CHAT_ID, mapper, id);
    }

    @Override
    public void save(Message message) {
        jdbcTemplate.update(SQL_SAVE_MESSAGE, message.getChatId(), message.getText(), message.getLogin());
    }
}
