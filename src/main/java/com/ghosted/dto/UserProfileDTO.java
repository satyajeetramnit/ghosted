package com.ghosted.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileDTO {
    private String name;
    private String email;
    private String phone;
    private String location;
    private String linkedin;
    private String github;
    private String portfolio;
    private String leetcode;
    private String hackerrank;
    private String currentTitle;
    private String yearsExp;
    private String existingSummary;
    // Stored as raw JSON strings (arrays) so we don't need separate tables
    private String experiencesJson;
    private String educationJson;
    private String projectsJson;
}
