package com.ghosted.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SavedResumeDTO {
    private String id;
    private String companyName;
    private String jobTitle;
    private String applicationId;
    private String resumeDataJson;
    private String latexCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
