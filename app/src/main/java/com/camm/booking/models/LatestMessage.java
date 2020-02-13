package com.camm.booking.models;

public class LatestMessage {
    private String message;
    private String date;

    public LatestMessage() {
        // default for Firebase Database
    }

    public LatestMessage(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

}
