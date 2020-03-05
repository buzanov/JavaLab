package ru.itis.javalab.service;

import org.springframework.stereotype.Component;


public interface EmailConfirmationService {
    void confirm(String code);
}
