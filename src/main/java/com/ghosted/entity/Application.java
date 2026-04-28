package com.ghosted.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "applications", indexes = {
        @Index(name = "idx_application_user_id", columnList = "user_id"),
        @Index(name = "idx_application_status", columnList = "status"),
        @Index(name = "idx_application_user_status", columnList = "user_id, status")
})
@Getter
@Setter
@NoArgsConstructor
public class Application extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /** Multiple contacts can be linked to one application. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "application_contacts",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<Contact> contacts = new HashSet<>();

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_url", columnDefinition = "TEXT")
    private String jobUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "applied_date", nullable = false)
    private LocalDate appliedDate = LocalDate.now();

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;
}
