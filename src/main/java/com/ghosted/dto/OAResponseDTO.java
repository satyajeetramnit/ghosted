package com.ghosted.dto;

import com.ghosted.entity.OAStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OAResponseDTO {
    private UUID id;
    private String platform;
    private LocalDateTime deadline;
    private OAStatus status;
    private String notes;
}
