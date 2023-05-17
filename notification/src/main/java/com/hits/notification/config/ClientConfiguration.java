package com.hits.notification.config;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;

public class ClientConfiguration {
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
