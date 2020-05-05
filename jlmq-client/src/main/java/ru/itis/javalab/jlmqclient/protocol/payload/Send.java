package ru.itis.javalab.jlmqclient.protocol.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Send {
    String name;
    String content;
    String messageId;
}
