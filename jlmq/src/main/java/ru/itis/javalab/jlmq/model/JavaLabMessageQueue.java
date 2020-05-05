package ru.itis.javalab.jlmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.Deque;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JavaLabMessageQueue {
    private String name;
    private Deque<Message> deque;
    private WebSocketSession session;
    private boolean isLast;
}
