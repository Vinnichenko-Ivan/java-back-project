package com.hits.common.service;

import com.hits.common.exception.ForbiddenException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyProvider {

    @Getter
    private final String key;

    public ApiKeyProvider(@Value("${api.key}") String key) {
        this.key = key;
    }

    public void checkKey(String key) {
        if(!key.equals(this.key)){
            throw new ForbiddenException();
        }
    }

}
