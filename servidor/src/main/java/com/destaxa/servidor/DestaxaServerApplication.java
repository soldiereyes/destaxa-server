package com.destaxa.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DestaxaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DestaxaServerApplication.class, args);

        new Thread(() -> {
            try {
                ServerSocketHandler serverSocketHandler = new ServerSocketHandler(5000);
                serverSocketHandler.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
