package com.camm.booking.models;

public class User {
    private String userName;
    private String userImage;

    public User() {
        // default for Firebase Database
    }

    public User(String userName, String userImage) {
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }
}
