package ru.itis.javalab.jlmqclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JlmqClientApplication {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(JlmqClientApplication.class, args);
        JlmqConsumer consumer;
        consumer = new JlmqConsumer()
                .onFail(System.out::println)
                .onSuccess(System.out::println)
                .handledClass(String.class)
                .connect();

        consumer.create("queue");
        for (int i = 0; i < 10; i++) {
            consumer.add(i);
        }
        consumer.subscribe("queue");

    }

}
