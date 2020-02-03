package ru.javalab.homework5.models;

public class JsonMessage {
    String Header;
    Object payload;


    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
