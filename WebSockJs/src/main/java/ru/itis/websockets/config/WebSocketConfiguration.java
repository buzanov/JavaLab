package ru.itis.websockets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.itis.websockets.handlers.AuthHandshakeHandler;
import ru.itis.websockets.handlers.WebSocketsMessagesHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketsMessagesHandler messagesHandler;

    @Autowired
    private AuthHandshakeHandler handshakeHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(messagesHandler, "/chat")
                .setHandshakeHandler(handshakeHandler).setAllowedOrigins("*").withSockJS();
    }
}
