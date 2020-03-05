package ru.itis.javalab.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.javalab.config.ApplicationContextConfig;
import ru.itis.javalab.config.FreemarkerConfig;
import ru.itis.javalab.model.User;
import ru.itis.javalab.repository.ConfirmRepository;
import ru.itis.javalab.repository.UserRepository;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MessageSender messageSender;
    @Autowired
    ConfirmRepository confirmRepository;

    @Override
    public boolean signUp(User user, ServletContext servletContext) {
        Long id;
        if ((id = userRepository.save(user).getId()) != null) {
            try {
                Template template = FreemarkerConfig.getInstance(servletContext.getRealPath("/WEB-INF/templates")).getTemplate("email-confirm.ftl");
                Map<String, Object> map = new HashMap<>();
                map.put("login", user.getLogin());
                String token = generateRandomStringByUUID();
                confirmRepository.save(token, id);
                map.put("token", token);
                String message = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
                messageSender.sendMessage(user.getEmail(), "Email confirmation", message);
                return true;
            } catch (IOException | TemplateException e) {
                throw new IllegalArgumentException(e);

            }
        }
        return false;
    }

    public static String generateRandomStringByUUID() {
        return UUID.randomUUID().toString();
    }
}
