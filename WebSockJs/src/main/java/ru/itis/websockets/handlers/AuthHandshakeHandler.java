package ru.itis.websockets.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;
import ru.itis.websockets.repository.UserRepository;

import java.util.Map;
import java.util.Objects;

@Component
public class AuthHandshakeHandler implements HandshakeHandler {

    private DefaultHandshakeHandler defaultHandshakeHandler = new DefaultHandshakeHandler();

    @Autowired
    private UserRepository usersRepository;

    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
        String cookie = Objects.requireNonNull(WebUtils.getCookie(request.getServletRequest(), "USER_LOGIN")).getValue();
        if (usersRepository.findUserByLogin(cookie).isPresent()) {
            return defaultHandshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }
}
