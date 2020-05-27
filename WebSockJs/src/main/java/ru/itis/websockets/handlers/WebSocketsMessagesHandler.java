package ru.itis.websockets.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.websockets.model.Message;
import ru.itis.websockets.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketsMessagesHandler extends TextWebSocketHandler {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Map<Long, List<WebSocketSession>> sessions;


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Message m = objectMapper.readValue(message.getPayload().toString(), Message.class);
        sessions.computeIfAbsent(m.getChatId(), k -> new ArrayList<>());
        if (!sessions.get(m.getChatId()).contains(session)) {
            sessions.get(m.getChatId()).add(session);
        }
        for (WebSocketSession webSocketSession : sessions.get(m.getChatId())) {
            webSocketSession.sendMessage(message);
        }
        messageRepository.save(m);
    }
}
