package ru.itis.servlets.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileSaver {
    void save(MultipartFile file);
}
