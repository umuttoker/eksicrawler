package com.hasssektor.util;

import com.hasssektor.bean.Config;
import com.hasssektor.eksiapi.ApiException;
import com.hasssektor.eksiapi.apis.AuthApi;
import com.hasssektor.eksiapi.models.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by umut on 4.08.2017.
 */
@Service
public class TokenUtil {

    Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

    @Autowired
    Config config;

    public void renewToken(){
        LOGGER.info("Access token yenileniyor");
        AuthApi authApi = new AuthApi();
        TokenResponse accessToken = null;
        try {
            accessToken = authApi.getAccessToken(config.getGrantType(), config.getUserName(), config.getPassword(), config.getClientSecret());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        if(accessToken != null) {
            config.API_KEY = accessToken.getTokenType() + " " + accessToken.getAccessToken();
            LOGGER.info("Access token yenilendi : "+ config.API_KEY);
        }
    }

}
