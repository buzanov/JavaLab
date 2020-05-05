package ru.itis.javalab.jlmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.javalab.jlmq.protocol.Header;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    Header status;
    String content;
    String messageId;
}
