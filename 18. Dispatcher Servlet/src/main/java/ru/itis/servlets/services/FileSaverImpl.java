package ru.itis.servlets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@PropertySource("classpath:application.properties")
public class FileSaverImpl implements FileSaver {
    @Autowired
    Environment environment;

    @Override
    public void save(MultipartFile multipartFile) {
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(environment.getProperty("upload.path") + multipartFile.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
