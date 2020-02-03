package ru.javalab.homework5.repository;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet row);
}
