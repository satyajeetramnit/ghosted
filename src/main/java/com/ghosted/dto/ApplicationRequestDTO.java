package com.ghosted.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ApplicationRequestDTO {
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    private String jobUrl;

    /** Optional applied date; defaults to today when null. */
    private LocalDate appliedDate;

    /** Link one or more existing contacts by id. */
    private List<UUID> contactIds;

    /** Inline creation: single new contact name (optional). */
    private String contactName;
    private String contactEmail;
}
