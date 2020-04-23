package ru.itis.websockets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.websockets.dto.LoginDto;
import ru.itis.websockets.model.User;
import ru.itis.websockets.repository.UserRepository;

import java.util.Optional;

@Component
public class SignInServiceImpl implements SignInService {

    @Autowired
    private UserRepository usersRepository;

    @Override
    public void login(LoginDto loginForm) {
        Optional<User> userOptional = usersRepository.findUserByLogin(loginForm.getLogin());
        System.out.println(userOptional.isPresent());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String pass = user.getPassword();
            if (!pass.equals(loginForm.getPassword())) {
                throw new IllegalArgumentException("Wrong password or login");
            }
        } else
            throw new IllegalArgumentException("User not found");
    }
}
