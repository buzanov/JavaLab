package dao;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    public static DBConnector getInstance() {
        return connector == null ? connector = new DBConnector() : connector;
    }

    static DBConnector connector;
    static Connection connection;

    public Connection getConnection() {
        return connection == null ? connection = getConnection0() : connection;
    }

    private Connection getConnection0() {
        try {
            InputStream is = getClass().getResourceAsStream("/app.properties");
            Properties properties = new Properties();
            properties.load(is);
            String url = properties.getProperty("db.url");
            String password = properties.getProperty("db.password");
            String login = properties.getProperty("db.user");
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } catch (FileNotFoundException e) {
            System.out.println("Can`t find properties file");
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            System.out.println("IO error");
            throw new IllegalArgumentException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("Cant find driver for jdbc");
            throw new IllegalArgumentException(e);
        }
    }
}
