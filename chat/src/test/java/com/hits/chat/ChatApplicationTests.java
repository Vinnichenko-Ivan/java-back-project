package com.hits.chat;

import com.hits.common.annotation.EnableApiKey;
import com.hits.common.annotation.EnableBaseExceptionHandler;
import com.hits.common.annotation.EnableJwt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableJwt
@SpringBootApplication
@EnableBaseExceptionHandler
@EnableApiKey
@EnableFeignClients
class ChatApplicationTests {

    @Test
    void contextLoads() {
    }

}
