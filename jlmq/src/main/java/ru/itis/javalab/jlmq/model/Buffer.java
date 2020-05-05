package ru.itis.javalab.jlmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Buffer {
    List<JavaLabMessageQueue> notSubscribed;
    Map<WebSocketSession, JavaLabMessageQueue> subscribed;

    public Buffer() {
        notSubscribed = new ArrayList<>();
        subscribed = new HashMap<>();
    }
}
