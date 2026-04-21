package com.ghosted.dto;

import com.ghosted.entity.OAStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OARequestDTO {
    // No @NotBlank — this DTO supports partial updates (upsert pattern)
    private String platform;

    private LocalDateTime deadline;
    private String notes;
    private OAStatus status;
}
