package ru.itis.javalab.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConfirmRepositoryImpl implements ConfirmRepository {
    @Autowired
    JdbcTemplate template;

    public static final String SQL_ADD_CONFIRMATION = "INSERT INTO public.confirmation_code(user_id, code) VALUES (?,?)";
    public static final String SQL_FIND_BY_CODE = "SELECT * FROM public.confirmation_code WHERE code = ?";
    public static final String SQL_DELETE_CODE = "DELETE FROM public.confirmation_code WHERE code = ?";

    RowMapper<Long> mapper = (row, i) ->
            row.getLong("user_id");

    @Override
    public Optional<String> find(Long aLong) {
        return Optional.empty();
    }

    @Override
    public String save(String s) {
        return null;
    }


    @Override
    public void save(String code, Long user_id) {
        template.update(SQL_ADD_CONFIRMATION, user_id, code);
    }

    @Override
    public void update(String s) {

    }

    @Override
    public void delete(Long aLong) {
    }
    @Override
    public void delete(String string) {
        template.update(SQL_DELETE_CODE, string);
    }

    @Override
    public Long findByCode(String code) {
        return template.queryForObject(SQL_FIND_BY_CODE, new Object[]{code}, mapper);
    }
}
