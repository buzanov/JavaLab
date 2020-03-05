package ru.itis.javalab.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.itis.javalab.model.User;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    JdbcTemplate template;
    RowMapper<User> mapper = (row, i) ->
            User.builder()
                    .email(row.getString("email"))
                    .login(row.getString("login"))
                    .password(row.getString("password"))
                    .id(row.getLong("id"))
                    .isConfirmed(row.getBoolean("isConfirmed"))
                    .build();

    public static final String SQL_ADD_USER = "INSERT INTO public.users(login, password, email) VALUES (?,?,?)";
    public static final String SQL_FIND_USER_BY_ID = "SELECT * FROM public.users WHERE id = ?";
    public static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM public.users WHERE email = ?";
    public static final String SQL_CONFIRM_USER = "UPDATE public.users SET isconfirmed = true WHERE id = ?";

    @Override
    public Optional<User> find(Long id) {
        return Optional.ofNullable(template.queryForObject(SQL_FIND_USER_BY_ID, new Object[]{id}, mapper));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(template.queryForObject(SQL_FIND_USER_BY_EMAIL, new Object[]{email}, mapper));
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_ADD_USER, new String[]{"id"});
                    ps.setString(1, user.getLogin());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getEmail());
                    return ps;
                }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void confirmUser(Long id) {
        template.update(SQL_CONFIRM_USER, id);
    }

    @Override
    public boolean isConfirmed(Long id) {
        return (boolean) template.queryForMap(SQL_FIND_USER_BY_ID, id).get("isConfirmed");
    }

    @Override
    public boolean isConfirmed(String email) {
        return findUserByEmail(email).get().isConfirmed();
    }

    @Override
    public void delete(Long aLong) {

    }
}
