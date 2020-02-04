package ru.javalab.homework7.protocol.json;

public class JsonMessage {
    private String Header;
    private Object payload;
    private String token = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
