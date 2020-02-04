package ru.javalab.homework7.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.context.Component;
import ru.javalab.homework7.dispatcher.RequestDispatcher;
import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Request;
import ru.javalab.homework7.protocol.json.JsonMessage;
import ru.javalab.homework7.services.JsonResolveService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiClientServer implements Component {
    private List<ClientHandler> clients = new ArrayList<>();

    public MultiClientServer() {

    }

    public void start(int port) {
        ServerSocket serverSocket;
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

    @Override
    public String getName() {
        return this.getClass().getName();
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
                    Request request = mapper.readValue(line, Request.class);
                    JsonResolveService resolveService = new JsonResolveService();
                    User user = resolveService.getUserFromToken(request.getToken());
                    JsonMessage response = null;
                    RequestDispatcher requestDispatcher = new RequestDispatcher();
                    requestDispatcher.doDispatch(request);

                    writer.println(mapper.writeValueAsString(response));
                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }


    }




}