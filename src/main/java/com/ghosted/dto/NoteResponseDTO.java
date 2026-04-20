package com.ghosted.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NoteResponseDTO {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
}
