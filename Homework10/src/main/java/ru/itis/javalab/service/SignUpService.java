package ru.itis.javalab.service;

import ru.itis.javalab.model.User;

import javax.servlet.ServletContext;

public interface SignUpService {
    boolean signUp(User user, ServletContext servletContext);
}
