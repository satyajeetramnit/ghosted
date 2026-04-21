package com.ghosted.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OARequestDTO {
    @NotBlank(message = "Platform is required")
    private String platform;

    private LocalDateTime deadline;
    private String notes;
}
