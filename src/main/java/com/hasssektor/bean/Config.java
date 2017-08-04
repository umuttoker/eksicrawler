package com.hasssektor.bean;

import org.springframework.stereotype.Component;

/**
 * Created by umut on 4.08.2017.
 */
@Component
public class Config {

    private String userName = "**";
    private String password = "**";
    private String clientSecret = "**";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        return "Config{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }
}
