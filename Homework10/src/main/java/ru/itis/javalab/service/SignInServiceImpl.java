package ru.itis.javalab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.javalab.model.User;
import ru.itis.javalab.repository.UserRepository;
import ru.itis.javalab.repository.UserRepositoryImpl;

import java.util.Optional;

@Component
public class SignInServiceImpl implements SignInService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean signIn(User user) {
        Optional<User> dbUser = userRepository.findUserByEmail(user.getEmail());
        if (dbUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword()))
                return true;
            else
                return false;
        } else
            return false;
    }
}
