package com.ghosted.dto;

import com.ghosted.entity.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterviewRequestDTO {
    @NotNull(message = "Interview type is required")
    private InterviewType type;

    private LocalDateTime scheduledAt;
    private String meetingLink;
    private String notes;
}
