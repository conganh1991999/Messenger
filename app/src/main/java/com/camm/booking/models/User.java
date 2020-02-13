package com.camm.booking.models;

public class User {
    private String userName;
    private String userImage;
    private String userId;

    public User() {
        // default for Firebase Database
    }

    public User(String userName, String userImage, String userId) {
        this.userName = userName;
        this.userImage = userImage;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserId() {
        return userId;
    }
}
