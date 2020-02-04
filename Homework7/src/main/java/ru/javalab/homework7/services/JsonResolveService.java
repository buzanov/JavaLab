package ru.javalab.homework7.services;

import ru.javalab.context.Component;
import ru.javalab.homework7.models.Message;
import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Request;

import java.util.Date;
import java.util.LinkedHashMap;

public class JsonResolveService implements Component {

    public User getUserFromToken(String token) {
        User user = null;
        if (token != null && !token.equals("")) {
            user = new AuthService().decodeToken(token);
        }
        return user;
    }

    public Message resolveMessageFromJson(User user, Request request) {
        LinkedHashMap payload = (LinkedHashMap) request.getPayload();
        return new Message(new Date(), user, (String) payload.get("message"));
    }

    public User getUserFromMessage(Request message) {
        LinkedHashMap payload = (LinkedHashMap) message.getPayload();
        return new User((String) payload.get("login"), (String) payload.get("password"));
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
