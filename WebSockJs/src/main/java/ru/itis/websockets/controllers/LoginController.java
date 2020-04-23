package ru.itis.websockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.websockets.dto.LoginDto;
import ru.itis.websockets.service.SignInService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
public class LoginController {

    @Autowired
    private SignInService loginService;

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(LoginDto loginForm, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            loginService.login(loginForm);
            Cookie userCookie = new Cookie("USER_LOGIN", loginForm.getLogin());
            userCookie.setMaxAge(24*60*60);
            response.addCookie(userCookie);
            modelAndView.setViewName("redirect:/chats");
        } catch(Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }
}
