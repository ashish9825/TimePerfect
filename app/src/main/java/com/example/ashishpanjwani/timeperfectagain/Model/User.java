package com.example.ashishpanjwani.timeperfectagain.Model;

import java.util.HashMap;

public class User {

    private String fullName;
    private String photo;
    private String email;
    private HashMap<String,Object> timestampJoined;

    public User() {

    }

    /**
     * USe this constructor to create new User.
     * Takes username, email and timestampJoined as params
     */

    public User(String mFullName, String mPhoto, String mEmail, HashMap<String,Object> timestampJoined) {
        this.fullName=mFullName;
        this.photo=mPhoto;
        this.email=mEmail;
        this.timestampJoined=timestampJoined;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }
}
