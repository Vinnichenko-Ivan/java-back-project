package com.hits.file;

import com.hits.common.annotation.EnableApiKey;
import com.hits.common.annotation.EnableBaseExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBaseExceptionHandler
@EnableApiKey
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }

}
