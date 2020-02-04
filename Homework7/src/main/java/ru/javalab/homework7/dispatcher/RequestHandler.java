package ru.javalab.homework7.dispatcher;

import ru.javalab.homework7.protocol.Request;
import ru.javalab.homework7.protocol.Response;

public class RequestHandler {
    RequestDispatcher dispatcher;

    public Response handle(Request request) {
        dispatcher.doDispatch(request);
    }
}
