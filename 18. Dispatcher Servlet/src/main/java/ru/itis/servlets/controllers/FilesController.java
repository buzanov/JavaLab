package ru.itis.servlets.controllers;

import javafx.scene.control.Separator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.servlets.models.FileInfo;
import ru.itis.servlets.repositories.FileRepository;
import ru.itis.servlets.services.FileSaver;

import java.io.*;
import java.util.Optional;

@Controller
@PropertySource("classpath:application.properties")
public class FilesController {
    @Autowired
    FileSaver fileSaver;
    @Autowired
    Environment environment;

    @Autowired
    FileRepository fileRepository;

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("email") String email) throws IOException {
        fileSaver.save(multipartFile.getBytes(), FileInfo.getInstance(multipartFile), email);
        return new ModelAndView("redirect:/upload");

    }

    // localhost:8080/files/123809183093qsdas09df8af.jpeg

    @RequestMapping(value = "/files/{file-name:.+}", method = RequestMethod.GET)
    public ResponseEntity getFile(@PathVariable("file-name") String fileName) {
        Optional<FileInfo> fileInfo = fileRepository.findByStorageFileName(fileName);
        if (fileInfo.isPresent()) {
            FileInfo info = fileInfo.get();
            File file = new File(environment.getProperty("upload.path") + File.separator + info.getStorageFileName());

            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.parseMediaType(info.getType()));
            respHeaders.setContentLength(info.getSize());
            respHeaders.setContentDispositionFormData("file", info.getOriginalFileName());
            try {
                InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
                return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/fileNotFound");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }
    }
}
