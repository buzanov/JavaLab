package ru.itis.websockets.service;


import ru.itis.websockets.dto.LoginDto;

public interface RegisterService {

    void register(LoginDto form);
}
