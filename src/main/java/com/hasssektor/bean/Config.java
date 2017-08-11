package com.hasssektor.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by umut on 4.08.2017.
 */
@Component
public class Config{

    @Value("${eksiusername}")
    private String userName = "**";
    @Value("${password}")
    private String password = "**";
    @Value("${clientsecret}")
    private String clientSecret = "**";
    @Value("${granttype}")
    private String grantType = "password";

    public static String API_KEY= null;

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

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @Override
    public String toString() {
        return "Config{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", grantType='" + grantType + '\'' +
                '}';
    }
}
