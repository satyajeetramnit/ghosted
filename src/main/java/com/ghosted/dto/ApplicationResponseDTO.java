package com.ghosted.dto;

import com.ghosted.entity.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
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

    /** All contacts linked to this application. */
    private List<ApplicationContactDTO> contacts;

    private java.util.List<InterviewResponseDTO> interviews;
    private OAResponseDTO oa;
}
