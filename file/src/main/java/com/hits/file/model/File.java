package com.hits.file.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class File {

    @Id
    private UUID id;

    private String name;

    private Date createdDate;

    private Date lastDownloadDate;

    @PrePersist
    public void generate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
}
