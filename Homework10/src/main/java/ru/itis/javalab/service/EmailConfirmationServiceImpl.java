package ru.itis.javalab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.itis.javalab.repository.ConfirmRepository;
import ru.itis.javalab.repository.UserRepository;


@Component
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    ConfirmRepository confirmRepository;
    @Autowired
    UserRepository userRepository;

    public void confirm(String code) {
        Long user_id = confirmRepository.findByCode(code);
        if (user_id != null) {
            confirmRepository.delete(user_id);
            userRepository.confirmUser(user_id);
        }
    }
}
