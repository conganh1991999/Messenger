package com.camm.booking.models;

public class Message {

    private String message;
    private String fromId;
    private String toId;
    private String messageId;
    private Long timestamp;

    public Message() {
        // default for get data from Firebase Database
    }

    public Message(String message, String fromId, String toId, String messageId, Long timestamp) {
        this.message = message;
        this.fromId = fromId;
        this.toId = toId;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getMessageId() {
        return messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
