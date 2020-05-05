package ru.itis.javalab.jlmqclient.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    Header status;
    String content;
    String messageId;
}
