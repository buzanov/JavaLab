package ru.javalab.homework7.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.javalab.homework7.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryImpl implements CrudRepository<User> {
    private Connection connection = new DBConnection().getConnection();
    Map<Integer, User> map = new HashMap<>();
    JdbcTemplate template;

    public static final String SQL_ADD = "INSERT INTO public.user (login, password) VALUES (?,?) ";
    public static final String SQL_FIND_BY_ID = "SELECT * FROM public.user WHERE id = ? ";
    public static final String SQL_FIND_BY_LOGIN = "SELECT * FROM public.user WHERE login = ?";
    public static final String SQL_FIND_BY_LOGIN_AND_PASSWORD = "SELECT * FROM public.user WHERE login = ? AND password = ?";


    private RowMapper<User> mapper = (rs, i) -> {
        try {
            Integer id = rs.getInt("id");
            map.put(id, new User(rs.getString("login"), rs.getString("password")).setId(id));
            return map.get(id);
        } catch (SQLException e) {
            System.out.println("User mapping error");
            throw new IllegalArgumentException(e);
        }
    };


    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(SQL_ADD, new Object[]{user.getLogin(), user.getPassword()}, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }


    @Override
    public Optional<User> find(User user) {
        return Optional.of(template.queryForObject(SQL_FIND_BY_LOGIN_AND_PASSWORD, new Object[]{user.getLogin(), user.getPassword()}, mapper));
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
        return template.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, mapper);
    }

    public User getUserByLogin(User user) {
        return template.queryForObject(SQL_FIND_BY_ID, new Object[]{user.getLogin()}, mapper);
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
