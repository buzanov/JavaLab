package ru.javalab.homework5.models;

public class ServerResponse extends Payload {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
}
