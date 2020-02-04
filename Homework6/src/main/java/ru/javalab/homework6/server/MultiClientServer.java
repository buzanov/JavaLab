package ru.javalab.homework6.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.homework6.models.Message;
import ru.javalab.homework6.models.Product;
import ru.javalab.homework6.models.User;
import ru.javalab.homework6.models.json.JsonMessage;
import ru.javalab.homework6.models.json.ServerResponse;
import ru.javalab.homework6.models.json.commands.GetMessages;
import ru.javalab.homework6.repository.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiClientServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private MessageRepositoryImpl messageRepository;
    private UserRepositoryImpl userRepository;

    public MultiClientServer(String dbPropFilePath) {
        DBConnection.setDbPropFilePath(dbPropFilePath);
        this.messageRepository = new MessageRepositoryImpl(dbPropFilePath);
        this.userRepository = new UserRepositoryImpl(dbPropFilePath);
        clients = new ArrayList<>();
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        while (true) {
            try {
                ClientHandler handler =
                        new ClientHandler(serverSocket.accept());
                handler.start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        ObjectMapper mapper = new ObjectMapper();


        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            System.out.println("New client!");
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(this.clientSocket.getOutputStream(), true);
                String line;
                while ((line = reader.readLine()) != null) {

                    JsonMessage message = mapper.readValue(line, JsonMessage.class);
                    JsonResolveService resolveService = new JsonResolveService();
                    User user = resolveService.getUserFromToken(message.getToken());

                    JsonMessage response = null;

                    switch (message.getHeader()) {
                        case ("Message"): {
                            if (user != null) {
                                messageLogic(user, message);
                                continue;
                            } else {
                                response = JsonPackageService.ERROR_MESSAGE_NOT_AUTHORIZED;
                            }
                            break;
                        }
                        case ("Login"): {
                            if (user == null) {
                                response = loginLogic(message);
                            } else {
                                response = JsonPackageService.ERROR_MESSAGE_ALREADY_LOGGED_IN;
                            }
                            break;
                        }
                        case ("Register"): {
                            if (user == null) {
                                response = registerLogic(message);
                            } else {
                                response = JsonPackageService.ERROR_MESSAGE_ALREADY_LOGGED_IN;
                            }
                            break;
                        }
                        case ("Command"): {
                            if (user == null) {
                                response = JsonPackageService.ERROR_MESSAGE_NOT_AUTHORIZED;
                            } else {
                                LinkedHashMap payload = (LinkedHashMap) message.getPayload();
                                String command = (String) payload.get("command");
                                switch (command) {
                                    case ("GetMessages"): {
                                        response = getMessages(message);
                                        break;
                                    }
                                    case ("BuyProduct"): {
                                        response = buyProduct(user, message);
                                        break;
                                    }
                                    case ("DeleteProduct"): {
                                        response = deleteProduct(user, message);
                                        break;
                                    }
                                    case ("GetMyProducts"): {
                                        response = getMyProducts(user);
                                        break;
                                    }
                                    case ("GetAllProducts"): {
                                        response = getAllProducts();
                                        break;
                                    }
                                    case ("AddProduct"): {
                                        response = addProduct(user, message);
                                        break;
                                    }

                                }

                            }
                        }
                    }
                    writer.println(mapper.writeValueAsString(response));
                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private JsonMessage addProduct(User user, JsonMessage message) {
            RoleRepositoryImpl roleRepository = RoleRepositoryImpl.getInstance();
            if (roleRepository.getPermissionByUserId(user.getId())) {
                LinkedHashMap payload = (LinkedHashMap) message.getPayload();
                LinkedHashMap addProduct = (LinkedHashMap) payload.get("information");
                Product product = new Product((int) addProduct.get("price"), (String) addProduct.get("name"));
                ProductRepositoryImpl productRepository = ProductRepositoryImpl.getInstance();
                productRepository.add(product);
                return JsonPackageService.SUCCESS_YOU_SUCCESSFULLY_ADDED_THE_PRODUCT;
            } else {
                return JsonPackageService.ERROR_PERMISSION;
            }
        }

        private JsonMessage getMyProducts(User user) {
            JsonMessage response = new JsonMessage();
            response.setHeader("Message");
            ProductRepositoryImpl productRepository = ProductRepositoryImpl.getInstance();
            List<Product> products = productRepository.getAllProductsByUser(user);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.setMessage(listOfProductsToString(products));
            response.setPayload(serverResponse);
            return response;
        }

        private JsonMessage getAllProducts() {
            JsonMessage response = new JsonMessage();
            response.setHeader("Products");
            ProductRepositoryImpl productRepository = ProductRepositoryImpl.getInstance();
            List<Product> products = productRepository.getAllProducts();
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.setMessage(listOfProductsToString(products));
            response.setPayload(serverResponse);
            return response;
        }

        private String listOfProductsToString(List<Product> products) {
            StringBuilder builder = new StringBuilder();
            for (Product product : products) {
                builder.append("[")
                        .append(product.getId())
                        .append("] ")
                        .append(product.getName())
                        .append(" price: ")
                        .append(product.getPrice())
                        .append("\n");
            }
            return builder.toString();
        }


        private JsonMessage deleteProduct(User user, JsonMessage message) {
            RoleRepositoryImpl roleRepository = RoleRepositoryImpl.getInstance();
            if (roleRepository.getPermissionByUserId(user.getId())) {
                ProductRepositoryImpl productRepository = ProductRepositoryImpl.getInstance();
                LinkedHashMap payload = (LinkedHashMap) message.getPayload();
                productRepository.delete(new Product((int) payload.get("id")));
                return JsonPackageService.SUCCESS_YOU_SUCCESSFULLY_DELETED_PRODUCT;
            } else {
                return JsonPackageService.ERROR_PERMISSION;
            }
        }

        private JsonMessage buyProduct(User user, JsonMessage message) {
            ProductRepositoryImpl productRepository = ProductRepositoryImpl.getInstance();
            LinkedHashMap payload = (LinkedHashMap) message.getPayload();
            Product product = productRepository.getProductById((int) payload.get("id"));
            productRepository.addOrder(user, product);
            return JsonPackageService.SUCCESS_YOU_SUCCESSFULLY_BOUGHT_PRODUCT;
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

        private JsonMessage registerLogic(JsonMessage message) {
            JsonResolveService resolveService = new JsonResolveService();
            User user = resolveService.getUserFromMessage(message);
            JsonMessage response;
            User dbUser = registration(user);
            if (dbUser != null) {
                response = JsonPackageService.SUCCESS_YOU_SUCCESSFULLY_REGISTERED;
            } else
                response = JsonPackageService.ERROR_MESSAGE_THIS_LOGIN_ALREADY_IN_USE;
            return response;
        }

        private JsonMessage messageLogic(User user, JsonMessage jsonMessage) {
            JsonResolveService resolveService = new JsonResolveService();
            Message receivedMessage = resolveService.resolveMessageFromJson(user, jsonMessage);
            JsonMessage response = JsonPackageService.buildMessage(buildMessage(receivedMessage));
            try {
                String serverAnswer = mapper.writeValueAsString(response);
                for (ClientHandler client : clients) {
                    if (client.clientSocket.isClosed()) {
                        clients.remove(client);
                        continue;
                    }
                    PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(), true);
                    out.println(serverAnswer);
                }
            } catch (JsonProcessingException e) {
                System.out.println("Error during json processing");
                throw new IllegalArgumentException(e);
            } catch (IOException e) {
                System.out.println("Error during creating print writer");
                e.printStackTrace();
            }
            writeInDB(receivedMessage);
            return response;
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
                clients.add(this);
            }
            return dbUser;
        }

        private JsonMessage loginLogic(JsonMessage jsonMessage) {
            JsonMessage response;
            JsonResolveService resolveService = new JsonResolveService();
            User user = resolveService.getUserFromMessage(jsonMessage);
            User dbUser = authorization(user);
            if (dbUser != null) {
                response = JsonPackageService.loggedIn(dbUser);
            } else {
                response = JsonPackageService.ERROR_MESSAGE_WRONG_LOGIN_OR_PASSWORD;
            }
            return response;

        }

        private User registration(User user) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user = new User(user.getLogin(), encoder.encode(user.getPassword()));
            User dbUser = null;
            if ((dbUser = userRepository.add(user)) != null) {
                clients.add(this);
                return dbUser;
            } else {
                return null;
            }
        }
    }


    private void writeInDB(Message message) {
        messageRepository.add(message);
    }


    private class JsonResolveService {

        private User getUserFromToken(String token) {
            User user = null;
            if (token != null && !token.equals("")) {
                user = new AuthService().decodeToken(token);
            }
            return user;
        }

        public Message resolveMessageFromJson(User user, JsonMessage message) {
            LinkedHashMap payload = (LinkedHashMap) message.getPayload();
            return new Message(new Date(), user, (String) payload.get("message"));
        }

        private User getUserFromMessage(JsonMessage message) {
            LinkedHashMap payload = (LinkedHashMap) message.getPayload();
            return new User((String) payload.get("login"), (String) payload.get("password"));
        }
    }

    private static class JsonPackageService {
        private static final JsonMessage ERROR_MESSAGE_ALREADY_LOGGED_IN = buildAnnouncementMessage("You already logged in");
        private static final JsonMessage ERROR_MESSAGE_NOT_AUTHORIZED = buildAnnouncementMessage("You are not authorized");
        private static final JsonMessage ERROR_MESSAGE_WRONG_LOGIN_OR_PASSWORD = buildAnnouncementMessage("Wrong login or password");
        private static final JsonMessage ERROR_PERMISSION = buildAnnouncementMessage("You have no permission for that");
        private static final JsonMessage ERROR_MESSAGE_THIS_LOGIN_ALREADY_IN_USE = buildAnnouncementMessage("This login already in use");
        private static final JsonMessage SUCCESS_YOU_SUCCESSFULLY_REGISTERED = buildAnnouncementMessage("You successfully registered");
        private static final JsonMessage SUCCESS_YOU_SUCCESSFULLY_LOGGED_IN = buildAnnouncementMessage("You successfully logged in");
        private static final JsonMessage SUCCESS_YOU_SUCCESSFULLY_BOUGHT_PRODUCT = buildAnnouncementMessage("You successfully bought the product");
        private static final JsonMessage SUCCESS_YOU_SUCCESSFULLY_ADDED_THE_PRODUCT = buildAnnouncementMessage("You successfully added the product");
        private static final JsonMessage SUCCESS_YOU_SUCCESSFULLY_DELETED_PRODUCT = buildAnnouncementMessage("You successfully deleted the product");


        private static JsonMessage buildAnnouncementMessage(String message) {
            JsonMessage response = new JsonMessage();
            response.setHeader("Message");
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.setMessage(message);
            response.setPayload(serverResponse);
            return response;
        }

        private static JsonMessage buildMessage(String message) {
            JsonMessage response = new JsonMessage();
            response.setHeader("Message");
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.setMessage(message);
            response.setPayload(serverResponse);
            return response;
        }

        private static JsonMessage loggedIn(User user) {
            JsonMessage response = SUCCESS_YOU_SUCCESSFULLY_LOGGED_IN;
            AuthService authService = new AuthService();
            response.setToken(authService.getToken(user));
            return response;
        }

    }
}