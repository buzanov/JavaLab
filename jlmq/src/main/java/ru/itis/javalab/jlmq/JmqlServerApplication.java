package ru.itis.javalab.jlmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itis.javalab.jlmq.model.Buffer;

@Configuration
@SpringBootApplication(scanBasePackages = "ru.itis.javalab.jlmq")
public class JmqlServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JmqlServerApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Buffer buffer() {
        return new Buffer();
    }

}
