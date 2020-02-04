package ru.javalab.homework7.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.homework7.models.Message;
import ru.javalab.homework7.protocol.json.JsonMessage;
import ru.javalab.homework7.protocol.json.Login;
import ru.javalab.homework7.protocol.json.Register;
import ru.javalab.homework7.protocol.json.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedHashMap;

public class ChatClient {
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String token;
    private JsonPackageService packageService = new JsonPackageService();

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            new Thread(receiveMessagesTask).start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(String message) {
        JsonMessage jsonMessage = null;
        String[] strings = message.split(" ");
        switch (strings[0]) {
            case ("--login"): {
                jsonMessage = packageService.login(strings);
                break;
            }
            case ("--register"): {
                jsonMessage = packageService.register(strings);
                break;
            }
            case ("--getMessages"): {
                jsonMessage = packageService.getMessage(strings);
                break;
            }
            case ("--addProduct"): {
                jsonMessage = packageService.addProduct(strings);
                break;
            }
            case ("--getAllProducts"): {
                jsonMessage = packageService.getAllProducts();
                break;
            }
            case ("--getMyProducts"): {
                jsonMessage = packageService.getMyProducts();
                break;
            }
            case ("--deleteProduct"): {
                jsonMessage = packageService.deleteProduct(strings);
                break;
            }
            case ("--buyProduct"): {
                jsonMessage = packageService.buyProduct(strings);
                break;
            }
            default: {
                jsonMessage = packageService.message(message);
            }
        }
        try {
            if (jsonMessage == null) {
                System.out.println("Invalid syntax.");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                String jsonValue = mapper.writeValueAsString(jsonMessage);
                writer.println(jsonValue);
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error during mapping json object");
            throw new IllegalArgumentException(e);
        }
    }


    private Runnable receiveMessagesTask = new Runnable() {
        public void run() {
            while (true) {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonMessage response = mapper.readValue(message, JsonMessage.class);
                        if (response.getToken() != null && !response.getToken().equals("")) {
                            token = response.getToken();
                        }
                        LinkedHashMap payload = (LinkedHashMap) response.getPayload();
                        System.out.println(payload.get("message"));
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    };


    public class JsonPackageService {
        private JsonMessage message(String message) {
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Message");
            Message m = new Message(new Date(), message);
            jsonMessage.setPayload(m);
            return jsonMessage;
        }

        private JsonMessage buyProduct(String[] message) {
            if (message.length < 3)
                return null;
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Command");
            Command command = new Command();
            command.setCommand("BuyProduct");
            BuyProduct buyProduct = new BuyProduct();
            buyProduct.setName(message[2]);
            buyProduct.setId(Integer.parseInt(message[1]));
            command.setInformation(buyProduct);
            jsonMessage.setPayload(command);
            return jsonMessage;
        }

        private JsonMessage deleteProduct(String[] message) {
            if (message.length < 2)
                return null;
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Command");
            Command command = new Command();
            command.setCommand("DeleteProduct");
            DeleteProduct deleteProduct = new DeleteProduct();
            deleteProduct.setId(Integer.parseInt(message[1]));
            command.setInformation(deleteProduct);
            jsonMessage.setPayload(command);
            return jsonMessage;
        }

        private JsonMessage getMyProducts() {
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Command");
            Command command = new Command();
            command.setCommand("GetMyProducts");
            jsonMessage.setPayload(command);
            return jsonMessage;
        }

        private JsonMessage getAllProducts() {
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Command");
            Command command = new Command();
            command.setCommand("GetAllProducts");
            jsonMessage.setPayload(command);
            return jsonMessage;
        }

        private JsonMessage addProduct(String[] message) {
            if (message.length < 3)
                return null;
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Command");
            Command command = new Command();
            AddProduct addProduct = new AddProduct();
            command.setCommand("AddProduct");
            addProduct.setName(message[1]);
            addProduct.setPrice(Integer.parseInt(message[2]));
            command.setInformation(addProduct);
            jsonMessage.setPayload(command);
            return jsonMessage;
        }

        private JsonMessage login(String[] message) {
            if (message.length < 3)
                return null;
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Login");
            Login login = new Login();
            login.setLogin(message[1]);
            login.setPassword(message[2]);
            jsonMessage.setPayload(login);
            return jsonMessage;
        }

        private JsonMessage initJson() {
            JsonMessage jsonMessage = new JsonMessage();
            jsonMessage.setToken(token);
            return jsonMessage;
        }

        private JsonMessage register(String[] message) {
            if (message.length < 3)
                return null;
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Register");
            Register register = new Register();
            register.setLogin(message[1]);
            register.setPassword(message[2]);
            jsonMessage.setPayload(register);
            return jsonMessage;
        }

        private JsonMessage getMessage(String[] message) {
            if (message.length < 3)
                return null;
            JsonMessage jsonMessage = initJson();
            jsonMessage.setHeader("Command");
            Command command = new Command();
            command.setCommand("GetMessages");
            GetMessages getMessages = new GetMessages();
            getMessages.setSize(Integer.parseInt(message[1]));
            getMessages.setPage(Integer.parseInt(message[2]));
            command.setInformation(getMessages);
            jsonMessage.setPayload(command);
            return jsonMessage;
        }
    }


}
