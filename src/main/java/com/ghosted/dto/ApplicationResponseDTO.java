package com.ghosted.dto;

import com.ghosted.entity.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ApplicationResponseDTO {
    private UUID id;
    private String companyName;
    private String jobTitle;
    private String jobUrl;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate followUpDate;
    private String contactName;
    private UUID contactId;
    private String contactEmail;
    private String contactCategory;
    
    private java.util.List<InterviewResponseDTO> interviews;
    private OAResponseDTO onlineAssessment;
}
