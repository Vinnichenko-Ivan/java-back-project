package com.hits.file.service.impl;

import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.service.JwtProvider;
import com.hits.file.config.MinioConfig;
import com.hits.file.model.File;
import com.hits.file.repository.FileRepository;
import com.hits.file.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final JwtProvider jwtProvider;
    private final FileRepository fileRepository;

    @PostConstruct
    public void init() {
        log.info("start");
    }
    @Override
    @Transactional
    public UUID upload(byte[] file, String name) {
        UUID id = java.util.UUID.randomUUID();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .stream(new ByteArrayInputStream(file), file.length, -1)
                    .object(id.toString())
                    .build());
        } catch (Exception e) {
            throw new ExternalServiceErrorException("minio not work");
        }
        File fileInfo = new File();
        fileInfo.setId(id);
        fileInfo.setName(name);
        fileRepository.save(fileInfo);
        return id;
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> download(UUID id) {
        var args = GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(id.toString()).build();

        try (var in = minioClient.getObject(args)){
            File file = fileRepository.findById(id).orElseThrow(NotFoundException::new);
            file.setLastDownloadDate(new Date(System.currentTimeMillis()));//TODO удаление редкоиспользуемого
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-File-Name", file.getName());
            fileRepository.save(file);
            return new ResponseEntity<byte[]>(in.readAllBytes(), responseHeaders, HttpStatus.OK);
        }catch (Exception e){
            throw new ExternalServiceErrorException("minio not work");
        }
    }
}


