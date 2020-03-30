package ru.itis.javalab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.javalab.dto.SignUpDto;
import ru.itis.javalab.model.User;
import ru.itis.javalab.service.SignUpService;

@Controller
public class RegisterController {

    @Autowired
    SignUpService signUpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("register_page");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(SignUpDto dto) {
        signUpService.signUp(User.builder()
                .login(dto.getLogin())
                .hashPassword(passwordEncoder.encode(dto.getPassword()))
                .build());
        return new ModelAndView("redirect:/login");
    }
}
