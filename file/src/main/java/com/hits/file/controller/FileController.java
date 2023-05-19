package com.hits.file.controller;

import com.hits.file.service.FileService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Api
@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
@Validated
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/")
    private UUID upload(@RequestParam("file")MultipartFile file) {
        try {
            return fileService.upload(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}")
    private byte[] download(@PathVariable UUID id) {
        return fileService.download(id);
    }
}
