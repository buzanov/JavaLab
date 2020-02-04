package ru.javalab.homework6;

import ru.javalab.homework6.models.User;
import ru.javalab.homework6.repository.AuthService;
import ru.javalab.homework6.repository.DBConnection;

public class Main {
    public static void main(String[] args) {
        User user = new User("vladek", "admin", 6);
        DBConnection.setDbPropFilePath("C:\\Users\\Vladislav\\IdeaProjects\\JavaLab\\Homework6\\src\\main\\java\\ru\\javalab\\homework6\\bin\\db.properties");
        AuthService authService = new AuthService();
        System.out.println(authService.getToken(user));
    }
}
