package com.hits.friends;

import com.hits.common.annotation.EnableApiKey;
import com.hits.common.annotation.EnableBaseExceptionHandler;
import com.hits.common.annotation.EnableJwt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableJwt
@SpringBootApplication
@EnableBaseExceptionHandler
@EnableApiKey
@EnableFeignClients
public class FriendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsApplication.class, args);
	}

}
