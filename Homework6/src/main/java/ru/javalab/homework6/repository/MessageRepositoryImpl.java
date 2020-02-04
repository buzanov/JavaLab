package ru.javalab.homework6.repository;

import ru.javalab.homework6.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryImpl implements CrudRepository<Message> {
    private Connection connection;
    UserRepositoryImpl userRepository;

    private RowMapper<Message> mapper = rs -> {
        try {
            return new Message(rs.getString("date"), rs.getString("time"), rs.getString("message_text"), userRepository.getUserById(rs.getInt("user_id")));
        } catch (SQLException e) {
            System.out.println("User mapping error");
            throw new IllegalArgumentException(e);
        }
    };

    public MessageRepositoryImpl(String dbPropFilePath) {
        userRepository = new UserRepositoryImpl(dbPropFilePath);
        this.connection = new DBConnection(dbPropFilePath).getConnection();
    }

    public List<Message> getMessages(int size, int page) {
        try {
            List<Message> list = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.comment LIMIT ? OFFSET ?");
            statement.setInt(1, size);
            statement.setInt(2, size * page);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(mapper.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Error during getting messages from db");
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Message add(Message message) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO public.comment (user_id, date, time, message_text) VALUES (?,?,?,?)");
            statement.setInt(1, message.getUser().getId());
            statement.setString(2, message.getDate());
            statement.setString(3, message.getTime());
            statement.setString(4, message.getMessage());
            return message;
        } catch (SQLException e) {
            System.out.println("Error during adding comment in repository");
            throw new IllegalArgumentException(e);
        }
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
