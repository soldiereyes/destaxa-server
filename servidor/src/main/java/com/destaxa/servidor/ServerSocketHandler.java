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
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String rawMessage = input.readLine();
            System.out.println("Message received: " + rawMessage);

            ISO8583Message requestMessage = ISO8583Message.parseMessage(rawMessage);
            ISO8583Message responseMessage = new ISO8583Message("0210");
            String value = requestMessage.getField(4);

            if (value == null || Double.parseDouble(value) < 0) {
                responseMessage.setField(39, "051"); // Negative transaction → Declined
            } else if (Double.parseDouble(value) > 1000) {
                System.out.println("Timeout simulated for value > 1000");
                return;
            } else {
                responseMessage.setField(39, "000"); // Approved
            }

            responseMessage.setField(4, value);
            output.println(responseMessage.buildMessage());
            System.out.println("Message sent: " + responseMessage.buildMessage());
        } finally {
            socket.close();
        }
    }
}
