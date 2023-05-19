package com.hits.file.repository;

import com.hits.file.model.File;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FileRepository extends CrudRepository<File, UUID> {
}
