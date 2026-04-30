package com.ghosted.controller;

import com.ghosted.dto.ApiResponse;
import com.ghosted.dto.SavedResumeDTO;
import com.ghosted.dto.UserProfileDTO;
import com.ghosted.entity.SavedResume;
import com.ghosted.entity.User;
import com.ghosted.entity.UserProfile;
import com.ghosted.repository.SavedResumeRepository;
import com.ghosted.repository.UserProfileRepository;
import com.ghosted.repository.UserRepository;
import com.ghosted.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles:
 *   GET/PUT  /profile           — user profile (personal info + template JSON)
 *   GET      /profile/resumes   — list saved resumes
 *   POST     /profile/resumes   — save new resume
 *   PUT      /profile/resumes/{id} — update saved resume
 *   DELETE   /profile/resumes/{id} — delete saved resume
 */
@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserProfileRepository profileRepo;

    @Autowired
    private SavedResumeRepository resumeRepo;

    @Autowired
    private UserRepository userRepo;

    // ─────────────────────────────────────────────────────────────────────────
    // Profile
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileDTO>> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID userId = userDetails.getId();
        UserProfile profile = profileRepo.findByUserId(userId)
                .orElse(null);

        if (profile == null) {
            // Return an empty profile — not a 404
            return ResponseEntity.ok(ApiResponse.success(new UserProfileDTO(), "Profile not yet configured"));
        }

        return ResponseEntity.ok(ApiResponse.success(toProfileDTO(profile), "Profile retrieved"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserProfileDTO>> upsertProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserProfileDTO dto) {

        UUID userId = userDetails.getId();
        User user = userRepo.findById(userId).orElseThrow();

        UserProfile profile = profileRepo.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return p;
                });

        applyProfileDTO(profile, dto);
        profileRepo.save(profile);

        return ResponseEntity.ok(ApiResponse.success(toProfileDTO(profile), "Profile saved"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Saved Resumes
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/resumes")
    public ResponseEntity<ApiResponse<List<SavedResumeDTO>>> listResumes(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<SavedResumeDTO> resumes = resumeRepo
                .findByUserIdOrderByCreatedAtDesc(userDetails.getId())
                .stream()
                .map(this::toResumeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(resumes, "Resumes retrieved"));
    }

    @PostMapping("/resumes")
    public ResponseEntity<ApiResponse<SavedResumeDTO>> createResume(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody SavedResumeDTO dto) {

        User user = userRepo.findById(userDetails.getId()).orElseThrow();

        SavedResume resume = new SavedResume();
        resume.setUser(user);
        applyResumeDTO(resume, dto);
        resumeRepo.save(resume);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(toResumeDTO(resume), "Resume saved"));
    }

    @PutMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<SavedResumeDTO>> updateResume(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id,
            @RequestBody SavedResumeDTO dto) {

        SavedResume resume = resumeRepo.findByIdAndUserId(id, userDetails.getId())
                .orElseThrow(() -> new AccessDeniedException("Resume not found or access denied"));

        applyResumeDTO(resume, dto);
        resumeRepo.save(resume);

        return ResponseEntity.ok(ApiResponse.success(toResumeDTO(resume), "Resume updated"));
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id) {

        SavedResume resume = resumeRepo.findByIdAndUserId(id, userDetails.getId())
                .orElseThrow(() -> new AccessDeniedException("Resume not found or access denied"));

        resumeRepo.delete(resume);
        return ResponseEntity.ok(ApiResponse.success(null, "Resume deleted"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private UserProfileDTO toProfileDTO(UserProfile p) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setName(p.getName());
        dto.setEmail(p.getEmail());
        dto.setPhone(p.getPhone());
        dto.setLocation(p.getLocation());
        dto.setLinkedin(p.getLinkedin());
        dto.setGithub(p.getGithub());
        dto.setPortfolio(p.getPortfolio());
        dto.setLeetcode(p.getLeetcode());
        dto.setHackerrank(p.getHackerrank());
        dto.setCurrentTitle(p.getCurrentTitle());
        dto.setYearsExp(p.getYearsExp());
        dto.setExistingSummary(p.getExistingSummary());
        dto.setExperiencesJson(p.getExperiencesJson());
        dto.setEducationJson(p.getEducationJson());
        dto.setProjectsJson(p.getProjectsJson());
        dto.setPreferencesJson(p.getPreferencesJson());
        return dto;
    }

    private void applyProfileDTO(UserProfile p, UserProfileDTO dto) {
        if (dto.getName() != null) p.setName(dto.getName());
        if (dto.getEmail() != null) p.setEmail(dto.getEmail());
        if (dto.getPhone() != null) p.setPhone(dto.getPhone());
        if (dto.getLocation() != null) p.setLocation(dto.getLocation());
        if (dto.getLinkedin() != null) p.setLinkedin(dto.getLinkedin());
        if (dto.getGithub() != null) p.setGithub(dto.getGithub());
        if (dto.getPortfolio() != null) p.setPortfolio(dto.getPortfolio());
        if (dto.getLeetcode() != null) p.setLeetcode(dto.getLeetcode());
        if (dto.getHackerrank() != null) p.setHackerrank(dto.getHackerrank());
        if (dto.getCurrentTitle() != null) p.setCurrentTitle(dto.getCurrentTitle());
        if (dto.getYearsExp() != null) p.setYearsExp(dto.getYearsExp());
        if (dto.getExistingSummary() != null) p.setExistingSummary(dto.getExistingSummary());
        if (dto.getExperiencesJson() != null) p.setExperiencesJson(dto.getExperiencesJson());
        if (dto.getEducationJson() != null) p.setEducationJson(dto.getEducationJson());
        if (dto.getProjectsJson() != null) p.setProjectsJson(dto.getProjectsJson());
        if (dto.getPreferencesJson() != null) p.setPreferencesJson(dto.getPreferencesJson());
    }

    private SavedResumeDTO toResumeDTO(SavedResume r) {
        SavedResumeDTO dto = new SavedResumeDTO();
        dto.setId(r.getId().toString());
        dto.setCompanyName(r.getCompanyName());
        dto.setJobTitle(r.getJobTitle());
        dto.setApplicationId(r.getApplicationId());
        dto.setResumeDataJson(r.getResumeDataJson());
        dto.setLatexCode(r.getLatexCode());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        return dto;
    }

    private void applyResumeDTO(SavedResume r, SavedResumeDTO dto) {
        if (dto.getCompanyName() != null) r.setCompanyName(dto.getCompanyName());
        if (dto.getJobTitle() != null) r.setJobTitle(dto.getJobTitle());
        r.setApplicationId(dto.getApplicationId()); // nullable, always set
        if (dto.getResumeDataJson() != null) r.setResumeDataJson(dto.getResumeDataJson());
        if (dto.getLatexCode() != null) r.setLatexCode(dto.getLatexCode());
    }
}
