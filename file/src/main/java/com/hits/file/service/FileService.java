package com.hits.file.service;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FileService {
    UUID upload(byte[] file, String name);


    ResponseEntity<byte[]> download(UUID id);
}
