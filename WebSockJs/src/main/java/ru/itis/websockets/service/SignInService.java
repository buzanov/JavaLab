package ru.itis.websockets.service;


import ru.itis.websockets.dto.LoginDto;

public interface SignInService {

    void login(LoginDto loginForm);
}
