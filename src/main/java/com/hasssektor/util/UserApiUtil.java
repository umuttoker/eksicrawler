package com.hasssektor.util;

import com.hasssektor.bean.Config;
import com.hasssektor.eksiapi.ApiClient;
import com.hasssektor.eksiapi.ApiException;
import com.hasssektor.eksiapi.apis.UserApi;
import com.hasssektor.eksiapi.models.User;
import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by umut on 4.08.2017.
 */
@Service
public class UserApiUtil {

    Logger LOGGER = LoggerFactory.getLogger(UserApiUtil.class);

    @Autowired
    private Config config;
    @Autowired
    TokenUtil tokenUtil;

    private ApiClient getApiClient(){
        ApiClient apiClient = new ApiClient();
        if(config.API_KEY == null)
            tokenUtil.renewToken();
        apiClient.setApiKey(config.API_KEY);
        OkHttpClient httpClient = apiClient.getHttpClient();
        httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        httpClient.setReadTimeout(60, TimeUnit.SECONDS);
        return apiClient;
    }


    public User getUser(String userNick){
        LOGGER.info("Suser detayi isteniyor userNick : "+userNick);
        UserApi userApi = new UserApi(getApiClient());
        User user = null;
        try {
            user = userApi.getUserByName(userNick);
        } catch (ApiException e) {
            e.printStackTrace();
            if(e.getCode() == 401) {
                tokenUtil.renewToken();
                return getUser(userNick);
            }
        }
        return user;
    }
}
