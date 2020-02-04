package ru.javalab.homework7.services;

import ru.javalab.context.Component;
import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Response;
import ru.javalab.homework7.protocol.json.JsonMessage;
import ru.javalab.homework7.protocol.json.ServerResponse;

public class JsonPackageService implements Component {
    public final JsonMessage ERROR_MESSAGE_ALREADY_LOGGED_IN = buildAnnouncementMessage("You already logged in");
    public final JsonMessage ERROR_MESSAGE_NOT_AUTHORIZED = buildAnnouncementMessage("You are not authorized");
    public final JsonMessage ERROR_MESSAGE_WRONG_LOGIN_OR_PASSWORD = buildAnnouncementMessage("Wrong login or password");
    public final JsonMessage ERROR_THIS_PRODUCT_DOEST_EXIST = buildAnnouncementMessage("This product doesnt exist");
    public final JsonMessage ERROR_PERMISSION = buildAnnouncementMessage("You have no permission for that");
    public final JsonMessage ERROR_MESSAGE_THIS_LOGIN_ALREADY_IN_USE = buildAnnouncementMessage("This login already in use");
    public final JsonMessage SUCCESS_YOU_SUCCESSFULLY_REGISTERED = buildAnnouncementMessage("You successfully registered");
    public final JsonMessage SUCCESS_YOU_SUCCESSFULLY_LOGGED_IN = buildAnnouncementMessage("You successfully logged in");
    public final JsonMessage SUCCESS_YOU_SUCCESSFULLY_BOUGHT_PRODUCT = buildAnnouncementMessage("You successfully bought the product");
    public final JsonMessage SUCCESS_YOU_SUCCESSFULLY_ADDED_THE_PRODUCT = buildAnnouncementMessage("You successfully added the product");
    public final JsonMessage SUCCESS_YOU_SUCCESSFULLY_DELETED_PRODUCT = buildAnnouncementMessage("You successfully deleted the product");


    private JsonMessage buildAnnouncementMessage(String message) {
        JsonMessage response = new JsonMessage();
        response.setHeader("Message");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setMessage(message);
        response.setPayload(serverResponse);
        return response;
    }

    public Response buildMessage(String message) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setMessage(message);
        return Response.build(serverResponse, Response.ResponseHeader.MESSAGE);
    }

    public Response loggedIn(User user) {
        JsonMessage response = SUCCESS_YOU_SUCCESSFULLY_LOGGED_IN;
        AuthService authService = new AuthService();
        response.setToken(authService.getToken(user));
        return response;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
