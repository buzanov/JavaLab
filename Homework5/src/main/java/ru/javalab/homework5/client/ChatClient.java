package ru.javalab.homework5.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.homework5.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedHashMap;

public class ChatClient {
    private User user;
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;

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
        JsonMessage jsonMessage = new JsonMessage();
        if (message.startsWith("--login")) {
            jsonMessage.setHeader("Login");
            String[] strings = message.split(" ");
            Login login = new Login();
            login.setLogin(strings[1]);
            login.setPassword(strings[2]);
            jsonMessage.setPayload(login);
            //login
        } else if (message.startsWith("--register")) {
            jsonMessage.setHeader("Register");
            String[] strings = message.split(" ");
            Register register = new Register();
            register.setLogin(strings[1]);
            register.setPassword(strings[2]);
            jsonMessage.setPayload(register);
            //registration
        } else if (message.startsWith("--getMessages")) {
            jsonMessage.setHeader("Command");
            String[] strings = message.split(" ");
            Command command = new Command();
            command.setSize(Integer.parseInt(strings[1]));
            command.setPage(Integer.parseInt(strings[2]));
            jsonMessage.setPayload(command);
            //get messages
        } else {
            jsonMessage.setHeader("Message");
            Message message1 = new Message(new Date(), message);
            jsonMessage.setPayload(message1);
            //default message.
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(jsonMessage);
            writer.println(jsonValue);
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
                        LinkedHashMap payload = (LinkedHashMap) response.getPayload();
                        System.out.println(payload.get("message"));
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    };

}
