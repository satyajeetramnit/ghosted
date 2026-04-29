package com.ghosted.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persisted user profile for the resume builder.
 * Stores all the "global" personal info and the structured
 * resume template (experiences, education, projects) as JSON.
 * One-to-one with User.
 */
@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ── Personal Info ────────────────────────────────────────
    @Column(name = "full_name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "location")
    private String location;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "github")
    private String github;

    @Column(name = "portfolio")
    private String portfolio;

    @Column(name = "leetcode")
    private String leetcode;

    @Column(name = "hackerrank")
    private String hackerrank;

    @Column(name = "current_title")
    private String currentTitle;

    @Column(name = "years_exp")
    private String yearsExp;

    @Column(name = "existing_summary", columnDefinition = "TEXT")
    private String existingSummary;

    // ── Resume Template (structured JSON) ────────────────────
    @Column(name = "experiences_json", columnDefinition = "TEXT")
    private String experiencesJson; // JSON array of ExperienceTemplate

    @Column(name = "education_json", columnDefinition = "TEXT")
    private String educationJson;   // JSON array of EducationTemplate

    @Column(name = "projects_json", columnDefinition = "TEXT")
    private String projectsJson;    // JSON array of ProjectTemplate
}
