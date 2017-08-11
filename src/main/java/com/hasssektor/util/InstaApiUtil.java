package com.hasssektor.util;

import com.hasssektor.instaapi.ApiClient;
import com.hasssektor.instaapi.ApiException;
import com.hasssektor.instaapi.apis.UserApi;
import com.hasssektor.instaapi.models.UserInfo;
import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by umut on 6.08.2017.
 */
@Service
public class InstaApiUtil {

    Logger LOGGER = LoggerFactory.getLogger(InstaApiUtil.class);

    private ApiClient getApiClient(){
        ApiClient apiClient = new ApiClient();
        OkHttpClient httpClient = apiClient.getHttpClient();
        httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        httpClient.setReadTimeout(60, TimeUnit.SECONDS);
        return apiClient;
    }

    public UserInfo getUserInfo(String username){
        LOGGER.info("User bilgisi aliniyor username : "+username);
        UserApi userApi = new UserApi(getApiClient());
        UserInfo userInfo = null;
        try {
            userInfo = userApi.getUserInfo(username, "1");
        } catch (ApiException e) {
            LOGGER.error("Biseyler olmadi ve detay alinamadi username : "+username);
        }
        return userInfo;
    }
}
