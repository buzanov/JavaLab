package ru.itis.javalab.jlmq.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.javalab.jlmq.dispatcher.MessageDispatcher;
import ru.itis.javalab.jlmq.protocol.Header;
import ru.itis.javalab.jlmq.protocol.JsonMessage;

@Component
public class WebSocketsMessagesHandler extends TextWebSocketHandler {
    @Autowired
    MessageDispatcher dispatcher;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(message.getPayload().toString());
        JsonMessage message1 = objectMapper.readValue(message.getPayload().toString(), JsonMessage.class);
        if (!dispatcher.doDispatch(session, message1)) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                    JsonMessage.builder()
                            .header(Header.ERROR.toString())
                            .payload("Error").build())));
        }
    }
}
