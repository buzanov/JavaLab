package ru.itis.javalab.jlmq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.itis.javalab.jlmq.connect.AuthHandshakeHandler;
import ru.itis.javalab.jlmq.connect.WebSocketsMessagesHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketsMessagesHandler messagesHandler;

    @Autowired
    private AuthHandshakeHandler handshakeHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(messagesHandler, "/jlmq")
                .setHandshakeHandler(handshakeHandler);
    }
}
