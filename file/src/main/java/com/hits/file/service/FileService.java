package com.hits.file.service;

import java.util.UUID;

public interface FileService {
    UUID upload(byte[] file);
    byte[] download(UUID id);
}
