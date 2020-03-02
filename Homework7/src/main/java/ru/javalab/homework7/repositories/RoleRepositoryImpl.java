package ru.javalab.homework7.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRepositoryImpl {
    private Connection connection = new DBConnection().getConnection();

    public boolean getPermissionByUserId(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT is_admin FROM role WHERE user_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            } else throw new IllegalArgumentException();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
