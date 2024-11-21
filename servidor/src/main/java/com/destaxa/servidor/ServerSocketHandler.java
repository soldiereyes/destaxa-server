package com.destaxa.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHandler {
    private final int port;

    public ServerSocketHandler(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection received from: " + socket.getInetAddress());

                new Thread(() -> {
                    try {
                        handleConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private void handleConnection(Socket socket) throws IOException {
        try (socket; BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String message = input.readLine();
            System.out.println("Message received: " + message);

            output.println("Message processed by the server.");
        }
    }
}
