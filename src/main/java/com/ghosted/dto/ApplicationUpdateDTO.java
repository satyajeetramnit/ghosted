package com.ghosted.dto;

import com.ghosted.entity.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Partial-update DTO for an existing Application. All fields optional;
 * only non-null fields are applied.
 */
@Getter
@Setter
public class ApplicationUpdateDTO {
    private String companyName;
    private String jobTitle;
    private String jobUrl;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate followUpDate;
}
