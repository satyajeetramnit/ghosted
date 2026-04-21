package com.ghosted.dto;

import com.ghosted.entity.InterviewStatus;
import com.ghosted.entity.InterviewType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InterviewResponseDTO {
    private UUID id;
    private InterviewType type;
    private InterviewStatus status;
    private LocalDateTime scheduledAt;
    private String meetingLink;
    private String notes;
}
