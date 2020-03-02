package ru.javalab.homework7.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.javalab.homework7.models.Message;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryImpl implements CrudRepository<Message> {
    private Connection connection = new DBConnection().getConnection();
    private UserRepositoryImpl userRepository;
    private JdbcTemplate template;

    public MessageRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public static final String SQL_SELECT_PAGINATION = "SELECT * FROM public.comment LIMIT ? OFFSET ?";
    public static final String SQL_ADD = "INSERT INTO public.comment (user_id, date, time, message_text) VALUES (?,?,?,?)";

    private RowMapper<Message> mapper = (rs, i) -> {
        try {
            return new Message(rs.getString("date"), rs.getString("time"), rs.getString("message_text"), userRepository.getUserById(rs.getInt("user_id")));
        } catch (SQLException e) {
            System.out.println("User mapping error");
            throw new IllegalArgumentException(e);
        }
    };


    public List<Message> getMessages(int size, int page) {
        return template.query(SQL_SELECT_PAGINATION, new Object[]{size, page}, mapper);
    }

    @Override
    public Message add(Message message) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(SQL_ADD, new Object[]{message.getUser().getId(), message.getDate(), message.getTime(), message.getMessage()}, keyHolder);
        message.setId(keyHolder.getKey().intValue());
        return message;
    }

    @Override
    public Optional<Message> find(Message message) {
        return Optional.empty();
    }

    @Override
    public boolean update(Message message) {
        return false;
    }

    @Override
    public boolean delete(Message message) {
        return false;
    }

}
