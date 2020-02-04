package ru.javalab.homework6.repository;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet row);
}
