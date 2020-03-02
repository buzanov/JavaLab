package ru.javalab.homework7.services;

import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Response;
import ru.javalab.homework7.protocol.json.JsonMessage;
import ru.javalab.homework7.protocol.json.ServerResponse;

public class JsonPackageService {
    public final Response ERROR_MESSAGE_ALREADY_LOGGED_IN = buildAnnouncementMessage("You already logged in");
    public final Response ERROR_MESSAGE_NOT_AUTHORIZED = buildAnnouncementMessage("You are not authorized");
    public final Response ERROR_MESSAGE_WRONG_LOGIN_OR_PASSWORD = buildAnnouncementMessage("Wrong login or password");
    public final Response ERROR_THIS_PRODUCT_DOEST_EXIST = buildAnnouncementMessage("This product doesnt exist");
    public final Response ERROR_PERMISSION = buildAnnouncementMessage("You have no permission for that");
    public final Response ERROR_MESSAGE_THIS_LOGIN_ALREADY_IN_USE = buildAnnouncementMessage("This login already in use");
    public final Response SUCCESS_YOU_SUCCESSFULLY_REGISTERED = buildAnnouncementMessage("You successfully registered");
    public final Response SUCCESS_YOU_SUCCESSFULLY_LOGGED_IN = buildAnnouncementMessage("You successfully logged in");
    public final Response SUCCESS_YOU_SUCCESSFULLY_BOUGHT_PRODUCT = buildAnnouncementMessage("You successfully bought the product");
    public final Response SUCCESS_YOU_SUCCESSFULLY_ADDED_THE_PRODUCT = buildAnnouncementMessage("You successfully added the product");
    public final Response SUCCESS_YOU_SUCCESSFULLY_DELETED_PRODUCT = buildAnnouncementMessage("You successfully deleted the product");


    private Response<String> buildAnnouncementMessage(String message) {
        return Response.build(message, Response.ResponseHeader.MESSAGE);
    }

    public Response buildMessage(String message) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setMessage(message);
        return Response.build(serverResponse, Response.ResponseHeader.MESSAGE);
    }

    public JsonMessage loggedIn(User user) {
        JsonMessage response = new JsonMessage();
        response.setHeader("Message");
        response.setPayload("You successfully registered");
        AuthService authService = new AuthService();
        response.setToken(authService.getToken(user));
        return response;
    }

}
