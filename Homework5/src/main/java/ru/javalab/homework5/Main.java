package ru.javalab.homework5;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Main {
    public static void main(String[] args) {
        String pass = "abcdef";
        //получение хэша по алгоритму bcrypt
        //полученное значение мы сохраняем в базу при регистрации
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(pass);
        System.out.println(hash);
        System.out.println(encoder.matches("admin", "$2a$10$KxkzYDq1d5sjwsJLULYNV.qJ.s6ILteC5HVE33IyTuoWGAvB3O1wm"));
    }
}
