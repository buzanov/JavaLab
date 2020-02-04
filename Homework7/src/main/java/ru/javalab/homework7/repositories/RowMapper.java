package ru.javalab.homework7.repositories;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet row);
}
