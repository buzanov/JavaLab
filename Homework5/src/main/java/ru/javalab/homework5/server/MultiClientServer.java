package ru.javalab.homework5.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.homework5.models.JsonMessage;
import ru.javalab.homework5.models.Message;
import ru.javalab.homework5.models.ServerResponse;
import ru.javalab.homework5.models.User;
import ru.javalab.homework5.repository.MessageRepositoryImpl;
import ru.javalab.homework5.repository.UserRepositoryImpl;

import javax.xml.ws.Response;
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
        private User user;
        private Socket clientSocket;
        private BufferedReader reader;


        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            System.out.println("New client!");
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonMessage message = mapper.readValue(line, JsonMessage.class);
                    LinkedHashMap payload = (LinkedHashMap) message.getPayload();
                    PrintWriter writer = new PrintWriter(this.clientSocket.getOutputStream(), true);
                    JsonMessage response = new JsonMessage();
                    switch (message.getHeader()) {
                        case ("Message"): {
                            if (user == null) {
                                response.setHeader("Message");
                                ServerResponse serverResponse = new ServerResponse();
                                serverResponse.setMessage("You are not authorized");
                                response.setPayload(serverResponse);
                                writer.println(mapper.writeValueAsString(response));
                                continue;
                            } else {
                                Message receivedMessage = new Message((String) payload.get("date"), (String) payload.get("time"), user, (String) payload.get("message"));
                                StringBuilder builder = new StringBuilder();
                                response.setHeader("Message");
                                ServerResponse serverResponse = new ServerResponse();
                                serverResponse.setMessage(builder.append(receivedMessage.getDate()).append(" ")
                                        .append(receivedMessage.getTime()).append(" ")
                                        .append(receivedMessage.getUser().getLogin()).append(" : ")
                                        .append(receivedMessage.getMessage()).toString());
                                response.setPayload(serverResponse);
                                String serverAnswer = mapper.writeValueAsString(response);
                                for (ClientHandler client : clients) {
                                    if (client.clientSocket.isClosed()) {
                                        clients.remove(client);
                                        continue;
                                    }
                                    PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(), true);
                                    out.println(serverAnswer);
                                }
                                writeInDB(receivedMessage);
                                break;
                            }
                        }
                        case ("Login"): {
                            User user1 = new User((String) payload.get("login"), (String) payload.get("password"));
                            response.setHeader("Message");
                            ServerResponse serverResponse = new ServerResponse();
                            if (authorization(user1)) {
                                serverResponse.setMessage("You are successfully logged in");
                            } else {
                                serverResponse.setMessage("Wrong login or password");
                            }
                            response.setPayload(serverResponse);
                            writer.println(mapper.writeValueAsString(response));
                            break;
                        }
                        case ("Register"): {
                            response.setHeader("Message");
                            ServerResponse serverResponse = new ServerResponse();
                            if (registration(new User((String) payload.get("login"), (String) payload.get("password")))) {
                                serverResponse.setMessage("You are successfully registered");
                            } else {
                                serverResponse.setMessage("Error");
                            }
                            response.setPayload(serverResponse);
                            writer.println(mapper.writeValueAsString(response));
                            break;
                        }
                        case ("Command"): {
                            if (user == null) {
                                response.setHeader("Message");
                                ServerResponse serverResponse = new ServerResponse();
                                serverResponse.setMessage("You are not authorized");
                                response.setPayload(serverResponse);
                                writer.println(mapper.writeValueAsString(response));
                                continue;
                            }
                            List<Message> list = messageRepository.getMessages((int) payload.get("size"), (int) payload.get("page"));
                            StringBuilder resp = new StringBuilder();
                            for (Message m : list) {
                                resp.append(m.getDate()).append(" ")
                                        .append(m.getTime()).append(" ")
                                        .append(m.getUser().getLogin()).append(" : ")
                                        .append(m.getMessage()).append("\n");
                            }
                            ServerResponse serverResponse = new ServerResponse();
                            serverResponse.setMessage(resp.toString());
                            response.setPayload(serverResponse);
                            writer.println(mapper.writeValueAsString(response));
                            break;
                        }
                    }

                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean authorization(User user) throws IOException {
            String password = userRepository.getPasswordByLogin(user.getLogin());
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(user.getPassword(), password)) {
                clients.add(this);
                this.user = user;
                return true;
            } else {
                return false;
            }
        }

        private void sendMessage(String string) throws IOException {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(string);
        }

        private void writeInDB(String message) {
            String text = message;
            Date date = new Date();
            Message currentMessage = new Message(date, this.user, text);
            messageRepository.add(currentMessage);
        }

        private void writeInDB(Message message) {
            messageRepository.add(message);
        }

        private boolean registration(User user) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user = new User(user.getLogin(), encoder.encode(user.getPassword()));
            if (userRepository.add(user)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
