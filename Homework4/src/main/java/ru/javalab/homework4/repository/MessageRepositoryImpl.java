package ru.javalab.homework4.repository;

import ru.javalab.homework4.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class MessageRepositoryImpl implements CrudRepository<Message> {
    private Connection connection;
    UserRepositoryImpl userRepository;

    public MessageRepositoryImpl(String dbPropFilePath) {
        userRepository = new UserRepositoryImpl(dbPropFilePath);
        this.connection = new DBConnection(dbPropFilePath).getConnection();
    }


    @Override
    public boolean add(Message message) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO public.comment (user_id, date, time, message_text) VALUES (?,?,?,?)");
            statement.setInt(1, userRepository.findIdByLogin(message.getUser().getLogin()));
            statement.setString(2, message.getDate());
            statement.setString(3, message.getTime());
            statement.setString(4, message.getMessage());
            return statement.execute();
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
