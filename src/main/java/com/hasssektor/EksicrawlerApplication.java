package com.hasssektor;

import com.hasssektor.bean.Config;
import com.squareup.okhttp.OkHttpClient;
import io.swagger.client.ApiClient;
import io.swagger.client.api.EntryApi;
import io.swagger.client.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@EnableAutoConfiguration
@SpringBootApplication
public class EksicrawlerApplication {

	@Autowired
	Config config;

	ApiClient getApiClient(){
		ApiClient apiClient = new ApiClient();
		apiClient.setApiKey("bearer ***");
		OkHttpClient httpClient = apiClient.getHttpClient();
		httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
		httpClient.setReadTimeout(60, TimeUnit.SECONDS);
		return apiClient;
	}

	@Bean
	public UserApi getUserApi(){
		return new UserApi(getApiClient());
	}

	@Bean
	public EntryApi getEntryApi(){
		return new EntryApi(getApiClient());
	}

	public static void main(String[] args) {
		SpringApplication.run(EksicrawlerApplication.class, args);
	}
}
