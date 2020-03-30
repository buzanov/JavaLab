package ru.itis.javalab.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.javalab.security.details.UserDetailsImpl;

@Controller
public class RootController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView rootPage() {
        String login = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getLogin();
        ModelAndView modelAndView = new ModelAndView("root_page");
        modelAndView.addObject("user", login);
        return modelAndView;
    }
}
