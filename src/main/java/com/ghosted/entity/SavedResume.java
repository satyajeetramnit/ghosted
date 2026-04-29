package com.ghosted.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A saved resume artifact — stores the generated resume data
 * (as JSON) and the LaTeX source code for a specific job application.
 */
@Entity
@Table(name = "saved_resumes")
@Getter
@Setter
@NoArgsConstructor
public class SavedResume extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "application_id")
    private String applicationId; // optional link to an application

    @Column(name = "resume_data_json", columnDefinition = "TEXT", nullable = false)
    private String resumeDataJson; // JSON serialized ResumeData

    @Column(name = "latex_code", columnDefinition = "TEXT", nullable = false)
    private String latexCode;
}
