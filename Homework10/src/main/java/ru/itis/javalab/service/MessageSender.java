package ru.itis.javalab.service;

import org.springframework.stereotype.Component;

public interface MessageSender {
    void sendMessage(String target, String topic, String message);
}
