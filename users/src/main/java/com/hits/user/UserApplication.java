package com.hits.user;

import com.hits.common.annotation.EnableBaseExceptionHandler;
import com.hits.common.annotation.EnableJwt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableJwt
@EnableBaseExceptionHandler
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
