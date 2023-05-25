package com.hits.file.controller;

import com.hits.file.model.File;
import com.hits.file.service.FileService;
import com.hits.file.service.impl.FileServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Api
@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
@Validated
@Log4j2
public class FileController {

    private final FileService fileService;

    @PostConstruct
    public void init() {
        log.info("start");
    }

    @PostMapping(value = "/")
    public UUID upload(@RequestParam("file")MultipartFile file, @RequestParam("name") String name) {
        try {
            return this.fileService.upload(file.getBytes(), name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> download(@PathVariable UUID id) {
        return fileService.download(id);
    }


}
