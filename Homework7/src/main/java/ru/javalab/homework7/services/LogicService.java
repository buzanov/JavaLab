package ru.javalab.homework7.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.context.Component;
import ru.javalab.homework7.models.Message;
import ru.javalab.homework7.models.Product;
import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Request;
import ru.javalab.homework7.protocol.Response;
import ru.javalab.homework7.protocol.json.JsonMessage;
import ru.javalab.homework7.protocol.json.ServerResponse;
import ru.javalab.homework7.repositories.MessageRepositoryImpl;
import ru.javalab.homework7.repositories.ProductRepositoryImpl;
import ru.javalab.homework7.repositories.RoleRepositoryImpl;
import ru.javalab.homework7.repositories.UserRepositoryImpl;

import java.util.LinkedHashMap;
import java.util.List;

public class LogicService implements Component {
    RoleRepositoryImpl roleRepository;
    ProductRepositoryImpl productRepository;
    JsonPackageService jsonPackageService;
    MessageRepositoryImpl messageRepository;
    UserRepositoryImpl userRepository;
    JsonResolveService resolveService;
    AuthService authService;


    private Response addProduct(User user, JsonMessage message) {
        if (roleRepository.getPermissionByUserId(user.getId())) {
            LinkedHashMap payload = (LinkedHashMap) message.getPayload();
            LinkedHashMap addProduct = (LinkedHashMap) payload.get("information");
            Product product = new Product((int) addProduct.get("price"), (String) addProduct.get("name"));
            productRepository.add(product);
            return Response.build(jsonPackageService.SUCCESS_YOU_SUCCESSFULLY_ADDED_THE_PRODUCT, Response.ResponseHeader.NOTIFICATION);
        } else {
            return Response.build(jsonPackageService.ERROR_PERMISSION, Response.ResponseHeader.ERROR);
        }
    }

    private Response getMyProducts(User user) {
        List<Product> products = productRepository.getAllProductsByUser(user);
        return Response.build(products, Response.ResponseHeader.LIST);
    }

    private Response getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        return Response.build(products, Response.ResponseHeader.LIST);
    }


    private Response deleteProduct(User user, JsonMessage message) {
        if (user != null) {
            if (roleRepository.getPermissionByUserId(user.getId())) {
                LinkedHashMap payload = (LinkedHashMap) message.getPayload();
                productRepository.delete(new Product((int) payload.get("id")));
                return Response.build(jsonPackageService.SUCCESS_YOU_SUCCESSFULLY_DELETED_PRODUCT, Response.ResponseHeader.NOTIFICATION);
            } else {
                return Response.build(jsonPackageService.ERROR_PERMISSION, Response.ResponseHeader.ERROR);
            }
        } else {
            return Response.build(jsonPackageService.ERROR_MESSAGE_NOT_AUTHORIZED, Response.ResponseHeader.ERROR);
        }
    }

    private Response buyProduct(User user, JsonMessage message) {
        LinkedHashMap payload = (LinkedHashMap) message.getPayload();
        Product product = productRepository.getProductById((int) payload.get("id"));
        if (product != null) {
            productRepository.addOrder(user, product);
            return Response.build(jsonPackageService.SUCCESS_YOU_SUCCESSFULLY_BOUGHT_PRODUCT, Response.ResponseHeader.NOTIFICATION)
        } else {
            return Response.build(jsonPackageService.ERROR_THIS_PRODUCT_DOEST_EXIST, Response.ResponseHeader.ERROR);
        }
    }

    private JsonMessage getMessages(JsonMessage message) {
        JsonMessage response = new JsonMessage();
        LinkedHashMap command = (LinkedHashMap) message.getPayload();
        LinkedHashMap getMessages = (LinkedHashMap) command.get("information");
        List<Message> list = messageRepository.getMessages((int) getMessages.get("size"), (int) getMessages.get("page"));
        StringBuilder resp = new StringBuilder();
        for (Message m : list) {
            resp.append(buildMessage(m)).append("\n");
        }
        response.setHeader("Message");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setMessage(resp.toString());
        response.setPayload(serverResponse);
        return response;
    }

    private Response registerLogic(Request message) {
        JsonResolveService resolveService = new JsonResolveService();
        User user = resolveService.getUserFromMessage(message);
        User dbUser = registration(user);
        if (dbUser != null) {
            return Response.build(jsonPackageService.SUCCESS_YOU_SUCCESSFULLY_REGISTERED, Response.ResponseHeader.NOTIFICATION);
        } else
            return Response.build(jsonPackageService.ERROR_MESSAGE_THIS_LOGIN_ALREADY_IN_USE, Response.ResponseHeader.ERROR);
    }

    public Response messageLogic(User user, Request request) {
        if (user != null) {
            Message receivedMessage = resolveService.resolveMessageFromJson(user, request);
            writeInDB(receivedMessage);
            return jsonPackageService.buildMessage(buildMessage(receivedMessage));
        } else {
            return Response.build(jsonPackageService.ERROR_MESSAGE_NOT_AUTHORIZED, Response.ResponseHeader.ERROR);
        }
    }

    private void writeInDB(Message message) {
        messageRepository.add(message);
    }

    private String buildMessage(Message message) {
        StringBuilder builder = new StringBuilder();
        builder.append(message.getDate()).append(" ")
                .append(message.getTime()).append(" ")
                .append(message.getUser().getLogin()).append(" : ")
                .append(message.getMessage()).toString();
        return builder.toString();
    }

    private User authorization(User user) {
        User dbUser = userRepository.getUserByLogin(user);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return dbUser;
        }
        return null;
    }

    private Response loginLogic(Request request) {
        JsonResolveService resolveService = new JsonResolveService();
        User user = resolveService.getUserFromMessage(request);
        User dbUser = authorization(user);
        if (dbUser != null) {
            return Response.build(authService.getToken(user), Response.ResponseHeader.SUCCESSFULLY_LOGGED_IN);
        } else {
            return Response.build(jsonPackageService.ERROR_MESSAGE_WRONG_LOGIN_OR_PASSWORD, Response.ResponseHeader.ERROR);
        }

    }

    private User registration(User user) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user = new User(user.getLogin(), encoder.encode(user.getPassword()));
        User dbUser;
        if ((dbUser = userRepository.add(user)) != null) {
            return dbUser;
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
