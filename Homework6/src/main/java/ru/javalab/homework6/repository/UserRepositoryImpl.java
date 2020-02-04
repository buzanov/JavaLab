package ru.javalab.homework6.repository;

import ru.javalab.homework6.models.User;

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
                    rs.getString("password")).setId(rs.getInt("id"));
        } catch (SQLException e) {
            System.out.println("User mapping error");
            throw new IllegalArgumentException(e);
        }
    };


    @Override
    public User add(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO public.user (login, password) VALUES (?,?) ");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            statement = connection.prepareStatement("SELECT * FROM public.user WHERE login = ?");
            statement.setString(1, user.getLogin());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapper.mapRow(rs);
            } else
                return null;
        } catch (SQLException e) {
            System.out.println("Error during adding user in db");
            e.printStackTrace();
            return null;
        }
    }

    public String getPasswordByLogin(String login) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT password FROM public.user WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error during getting password user");
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
            stmt = connection.prepareStatement("SELECT id FROM public.user WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error during executing method findIdByLogin()");
            throw new IllegalArgumentException(e);
        }
    }

    public User getUserById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM public.user WHERE id = ? ");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapper.mapRow(rs);
            } else return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public User getUserByLogin(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM public.user WHERE login = ?");
            stmt.setString(1, user.getLogin());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapper.mapRow(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
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
