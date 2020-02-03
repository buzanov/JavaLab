package ru.javalab.homework4.repository;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet row);
}
