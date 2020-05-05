package ru.itis.javalab.jlmqclient.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonMessage {
    String header;
    String  payload;
}
