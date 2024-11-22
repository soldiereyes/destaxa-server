package com.destaxa.servidor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHandler {
    private final int port;
    private static final Logger logger = LoggerFactory.getLogger(ServerSocketHandler.class);

    public ServerSocketHandler(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Connection received from: {}", serverSocket.getInetAddress());
            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Connection received from: {}", socket.getInetAddress());
                new Thread(() -> {
                    try {
                        handleConnection(socket);
                    } catch (IOException e) {
                        logger.error("Error handling connection: {}", e.getMessage());
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
            logger.debug("Raw message received: {}", rawMessage);
            ISO8583Message requestMessage = ISO8583Message.parseMessage(rawMessage);
            ISO8583Message responseMessage = new ISO8583Message("0210");
            String value = requestMessage.getField(4);

            if (value == null || Double.parseDouble(value) < 0) {
                responseMessage.setField(39, "051");
                logger.info("Transaction declined: Negative value. NSU: {}", requestMessage.getField(11));
            } else if (Double.parseDouble(value) > 1000) {
                logger.warn("Transaction timeout simulated for value > 1000. NSU: {}", requestMessage.getField(11));
                return;
            } else {
                responseMessage.setField(39, "000");
                logger.info("Transaction approved. NSU: {}", requestMessage.getField(11));
            }

            responseMessage.setField(4, value);
            output.println(responseMessage.buildMessage());
            logger.debug("Response sent: {}", responseMessage.buildMessage());
        } finally {
            socket.close();
        }
    }
}
