package com.hits.chat.dto;

import com.hits.chat.model.File;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class SendPrivateMessageDto {
    @NotNull
    private UUID user;
    @NotNull
    private String text;
    private Set<File> files;
}
