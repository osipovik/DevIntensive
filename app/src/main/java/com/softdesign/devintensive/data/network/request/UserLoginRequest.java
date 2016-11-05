package com.softdesign.devintensive.data.network.request;

/**
 * Created by OsIpoFF on 05.11.16.
 */
public class UserLoginRequest {

    private String email;
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
