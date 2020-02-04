package ru.javalab.homework7.protocol;

public class Response<T> {
    public enum ResponseHeader {
        MESSAGE, ERROR, NOTIFICATION, SUCCESSFULLY_LOGGED_IN, LIST
    }

    private ResponseHeader header;
    private T data;

    private Response(T data, ResponseHeader header) {
        this.data = data;
        this.header = header;
    }

    public static <E> Response<E> build(E data, ResponseHeader header) {
        return new Response<>(data, header);
    }
}