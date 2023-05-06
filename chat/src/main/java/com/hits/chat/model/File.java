package com.hits.chat.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.util.UUID;

@Data
@Embeddable
public class File {
    private UUID fileId;

    private String fileName;
}
