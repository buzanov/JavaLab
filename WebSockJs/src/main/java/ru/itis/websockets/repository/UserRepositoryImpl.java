package ru.itis.websockets.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itis.websockets.model.User;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    public static final String SQL_SAVE_USER = "INSERT INTO users(login, password) VALUES (?,?)";

    RowMapper<User> mapper = (rs, i) ->
            User.builder()
                    .id(rs.getLong("id"))
                    .login(rs.getString("login"))
                    .password(rs.getString("password"))
                    .build();

    @Override
    public Optional<User> findUserByLogin(String login) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_USER_BY_LOGIN, new Object[]{login}, mapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(SQL_SAVE_USER, user.getLogin(), user.getPassword());
    }
}
