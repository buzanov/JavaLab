package ru.itis.servlets.services;

import ru.itis.servlets.models.FileInfo;

public interface FileSaver {
    void save(byte[] bytes, FileInfo file, String email);
}
