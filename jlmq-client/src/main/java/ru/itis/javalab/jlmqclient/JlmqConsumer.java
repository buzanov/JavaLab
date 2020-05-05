package ru.itis.javalab.jlmqclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import ru.itis.javalab.jlmqclient.handler.WebSocketHandlerImpl;
import ru.itis.javalab.jlmqclient.protocol.Header;
import ru.itis.javalab.jlmqclient.protocol.JsonMessage;
import ru.itis.javalab.jlmqclient.protocol.payload.Create;
import ru.itis.javalab.jlmqclient.protocol.payload.Receive;
import ru.itis.javalab.jlmqclient.protocol.payload.Subscribe;

import java.io.Serializable;
import java.net.URI;
import java.util.function.Consumer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JlmqConsumer<T extends Serializable> {
    static String address = "ws://localhost/jlmq";
    WebSocketHandlerImpl<T> handler;
    String queue;
    WebSocketClient socketClient;
    WebSocketSession socketSession;
    @Autowired
    ObjectMapper mapper;
    private Consumer<T> fail;
    private Consumer<T> success;
    private Class<T> tClass;

    JlmqConsumer handledClass(Class<T> tClass) {
        this.tClass = tClass;
        return this;
    }

    JlmqConsumer onSuccess(Consumer consumer) {
        this.success = consumer;
        return this;
    }

    JlmqConsumer onFail(Consumer consumer) {
        this.fail = consumer;
        return this;
    }

    @SneakyThrows
    JlmqConsumer connect() {
        this.mapper = new ObjectMapper();
        this.socketClient = new StandardWebSocketClient();
        handler = new WebSocketHandlerImpl<>(fail, success, tClass);
        socketSession = socketClient.doHandshake(handler, new WebSocketHttpHeaders(), URI.create(address)).get();
        return this;
    }




    @SneakyThrows
    public void create(String name) {
        queue = name;
        socketSession.sendMessage(new TextMessage(
                mapper.writeValueAsString(
                        JsonMessage
                                .builder()
                                .header(Header.CREATE.toString())
                                .payload(mapper.writeValueAsString(Create
                                        .builder()
                                        .queueName(name)
                                        .build()))
                                .build())));
    }

    @SneakyThrows
    public void subscribe(String name) {
        queue = name;
        socketSession.sendMessage(new TextMessage(
                mapper.writeValueAsString(
                        JsonMessage
                                .builder()
                                .header(Header.SUBSCRIBE.toString())
                                .payload(mapper.writeValueAsString(Subscribe
                                        .builder()
                                        .queueName(name)
                                        .build()))
                                .build())));
    }

    @SneakyThrows
    public void add(T t) {
        socketSession.sendMessage(new TextMessage(
                mapper.writeValueAsString(
                        JsonMessage
                                .builder()
                                .header(Header.RECEIVE.toString())
                                .payload(mapper.writeValueAsString(Receive
                                        .builder()
                                        .content(mapper.writeValueAsString(t))
                                        .queueName(queue)
                                        .build()))
                                .build())));
    }
}
