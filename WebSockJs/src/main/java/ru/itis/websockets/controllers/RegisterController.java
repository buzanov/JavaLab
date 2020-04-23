package ru.itis.websockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.websockets.dto.LoginDto;
import ru.itis.websockets.service.RegisterService;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(LoginDto registerForm) {
        ModelAndView modelAndView = new ModelAndView();
        registerService.register(registerForm);
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }
}
