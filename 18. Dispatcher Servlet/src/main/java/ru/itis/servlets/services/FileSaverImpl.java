package ru.itis.servlets.services;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.servlets.models.FileInfo;
import ru.itis.servlets.repositories.FileRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@PropertySource("classpath:application.properties")
public class FileSaverImpl implements FileSaver {
    @Autowired
    FileRepository fileRepository;
    @Autowired
    Environment environment;

    @Override
    public void save(byte[] bytes, FileInfo fileInfo, String email) {
        try {
            FileCopyUtils.copy(bytes, new File(environment.getProperty("upload.path") + File.separator + fileInfo.getStorageFileName()));
            fileRepository.saveAndFlush(fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
