package com.destaxa.servidor;

import java.util.HashMap;
import java.util.Map;

public class ISO8583Message {
    private String messageType;
    private final Map<Integer, String> dataFields;

    public ISO8583Message(String messageType) {
        this.messageType = messageType;
        this.dataFields = new HashMap<>();

    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setField(int bit, String value) {
        dataFields.put(bit, value);
    }

    public String getField(int bit) {
        return dataFields.get(bit);
    }

    public String buildMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(messageType);

        for (int i = 1; i <= 128; i++) {
            String value = dataFields.get(i);
            if (value != null) {
                messageBuilder.append("|").append(i).append(":").append(value);
            }
        }
        return messageBuilder.toString();
    }

    public static ISO8583Message parseMessage(String rawMessage) {
        String[] parts = rawMessage.split("\\|");
        ISO8583Message message = new ISO8583Message(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            String[] field = parts[i].split(":");
            int bit = Integer.parseInt(field[0]);
            String value = field[1];
            message.setField(bit, value);
        }
        return message;
    }
}