package ru.itis.websockets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.websockets.dto.LoginDto;
import ru.itis.websockets.model.User;
import ru.itis.websockets.repository.UserRepository;

@Component
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserRepository usersRepository;

    @Override
    public void register(LoginDto form) {
        if (usersRepository.findUserByLogin(form.getLogin()).isPresent()) {
            throw new IllegalArgumentException("User with such login already exist!");
        } else
        usersRepository.save(User.builder()
                .password(form.getPassword())
                .login(form.getLogin())
                .build());
    }
}
