package ru.itis.javalab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.javalab.model.User;
import ru.itis.javalab.repository.UserRepository;

@Component
public class SignUpServiceImpl implements SignUpService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void signUp(User user) {
        userRepository.save(user);
    }
}
