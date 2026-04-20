package com.ghosted.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteRequestDTO {
    @NotBlank(message = "Content cannot be empty")
    private String content;
}
