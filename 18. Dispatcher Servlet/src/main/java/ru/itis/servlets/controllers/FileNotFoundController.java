package ru.itis.servlets.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileNotFoundController {
    @RequestMapping(value = "/fileNotFound", method = RequestMethod.GET)
    public ModelAndView fileNotFound() {
        return new ModelAndView("file_not_found");
    }
}
