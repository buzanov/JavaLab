package ru.itis.servlets.controllers;

import javafx.scene.control.Separator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.servlets.services.FileSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class FilesController {
    @Autowired
    FileSaver fileSaver;

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        fileSaver.save(multipartFile);
        return new ModelAndView("redirect:/upload");

    }

    // localhost:8080/files/123809183093qsdas09df8af.jpeg

    @RequestMapping(value = "/files/{file-name:.+}", method = RequestMethod.GET)
    public ModelAndView getFile(@PathVariable("file-name") String fileName) {
        // TODO: найти на диске
        // TODO: отдать пользователю
        return null;
    }
}
