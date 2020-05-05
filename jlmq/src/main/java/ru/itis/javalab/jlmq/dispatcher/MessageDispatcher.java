package ru.itis.javalab.jlmq.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.itis.javalab.jlmq.model.Buffer;
import ru.itis.javalab.jlmq.model.JavaLabMessageQueue;
import ru.itis.javalab.jlmq.model.Message;
import ru.itis.javalab.jlmq.protocol.JsonMessage;
import ru.itis.javalab.jlmq.protocol.payload.Accepted;
import ru.itis.javalab.jlmq.protocol.payload.Create;
import ru.itis.javalab.jlmq.protocol.payload.Receive;
import ru.itis.javalab.jlmq.protocol.payload.Subscribe;

import java.util.ArrayDeque;
import java.util.UUID;

import static ru.itis.javalab.jlmq.protocol.Header.*;

@Component
public class MessageDispatcher {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Buffer buffer;

    public boolean doDispatch(WebSocketSession session, JsonMessage message) {
        try {
            String h = message.getHeader();
//            System.out.println(message.getPayload().toString());
            if (h.equals(CREATE.toString())) {
                Create create = objectMapper.readValue(message.getPayload().toString(), Create.class);
                buffer.getNotSubscribed()
                        .add(JavaLabMessageQueue
                                .builder()
                                .name(create.getQueueName())
                                .deque(new ArrayDeque<Message>())
                                .build());
            } else if (h.equals(SUBSCRIBE.toString())) {
                Subscribe subscribe = objectMapper.readValue(message.getPayload().toString(), Subscribe.class);
                for (int i = 0; i < buffer.getNotSubscribed().size(); i++) {
                    if (buffer.getNotSubscribed().get(i).getName().equals(subscribe.getQueueName())) {
                        buffer.getSubscribed().put(session, buffer.getNotSubscribed().get(i));
                        buffer.getNotSubscribed().remove(i);
                        sendNext(session);
                        return true;
                    }
                }
                System.out.println("Can`t find queue with such name");
                return false;
            } else if (h.equals(ACCEPTED.toString())) {
                Accepted accepted = objectMapper.readValue(message.getPayload().toString(), Accepted.class);
                buffer.getSubscribed().get(session).getDeque().getFirst().setStatus(ACCEPTED);
                return true;
            } else if (h.equals(COMPLETED.toString())) {
                buffer.getSubscribed().get(session).getDeque().pop().setStatus(COMPLETED);
                sendNext(session);
                return true;
            } else if (h.equals(FAILED.toString())) {
                buffer.getSubscribed().get(session).getDeque().getFirst().setStatus(FAILED);
                return true;
            } else if (h.equals(RECEIVE.toString())) {
                Receive receive = objectMapper.readValue(message.getPayload().toString(), Receive.class);
                Message toAdd = Message.builder()
                        .content(receive.getContent())
                        .messageId(UUID.randomUUID().toString())
                        .build();

                if (buffer.getSubscribed().get(session) != null && buffer.getSubscribed().get(session).getName().equals(receive.getQueueName())) {
                    buffer.getSubscribed().get(session).getDeque().addLast(toAdd);
                    return true;
                }
                for (JavaLabMessageQueue javaLabMessageQueue : buffer.getNotSubscribed()) {
                    if (javaLabMessageQueue.getName().equals(receive.getQueueName())) {
                        javaLabMessageQueue.getDeque().addLast(toAdd);
                        return true;
                    }
                }
                for (JavaLabMessageQueue value : buffer.getSubscribed().values()) {
                    if (value.getName().equals(receive.getQueueName())) {
                        value.getDeque().addLast(toAdd);
                        return true;
                    }
                }
                return false;
            } else if (h.equals(SEND.toString())) {
                sendNext(session);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SneakyThrows
    private void sendNext(WebSocketSession session) {
        if (!buffer.getSubscribed().get(session).getDeque().isEmpty()) {
            Message toSend =
                    buffer.getSubscribed().get(session).getDeque().getFirst();
            JsonMessage jsonMessage = JsonMessage.builder()
                    .header(SEND.toString())
                    .payload(objectMapper.writeValueAsString(toSend))
                    .build();
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonMessage)));
        }
    }
}
