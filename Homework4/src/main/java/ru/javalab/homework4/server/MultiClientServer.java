package ru.javalab.homework4.server;

import ru.javalab.homework4.repository.MessageRepositoryImpl;
import ru.javalab.homework4.repository.UserRepositoryImpl;
import ru.javalab.homework4.models.Message;
import ru.javalab.homework4.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
                if (!authorization(reader)) {
                    sendMessage("Wrong login or password");
                    clientSocket.close();
                }
                String line;
                while ((line = reader.readLine()) != null) {
                    for (ClientHandler client : clients) {
                        if (client.clientSocket.isClosed()) {
                            clients.remove(client);
                            continue;
                        }
                        PrintWriter writer = new PrintWriter(client.clientSocket.getOutputStream(), true);
                        writer.println(user.getLogin() + ": " + line);
                        writeInDB(line);
                    }
                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean authorization(BufferedReader reader) throws IOException {
            sendMessage("SERVER: Enter your login: ");
            String login = reader.readLine();
            sendMessage("SERVER: Enter your password: ");
            String password = reader.readLine();
            User user = new User(login, password);
            if (userRepository.find(user).isPresent()) {
                clients.add(this);
                this.user = user;
                sendMessage("You are logged in");
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
            String login = user.getLogin();
            String text = message;
            Date date = new Date();
            Message currentMessage = new Message(date, new User(login), text);
            messageRepository.add(currentMessage);
        }
    }
}
