package ru.javalab.homework4.repository;

import ru.javalab.homework4.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements CrudRepository<User> {
    private Connection connection;

    public UserRepositoryImpl(String dbPropFilePath) {
        this.connection = new DBConnection(dbPropFilePath).getConnection();
    }

    private RowMapper<User> mapper = rs -> {
        try {
            return new User(rs.getString("login"),
                    rs.getString("password"));
        } catch (SQLException e) {
            System.out.println("User mapping error");
            throw new IllegalArgumentException(e);
        }
    };

    @Override
    public boolean add(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO public.user (login, password) VALUES (?,?)");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            return statement.execute();
        } catch (SQLException e) {
            System.out.println("Error during adding user in db");
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<User> find(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM public.user WHERE login = ? AND password = ?");
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(mapper.mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.out.println("Error during finding user");
            throw new IllegalArgumentException(e);
        }

    }

    public Integer findIdByLogin(String login) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("SELECT  id FROM  public.user WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Integer.parseInt(rs.getString("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error during executing method findIdByLogin()");
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }
}
