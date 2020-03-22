package ru.itis.servlets.services;

public interface MessageSender {
    void sendMessage(String target, String topic, String message);
}
