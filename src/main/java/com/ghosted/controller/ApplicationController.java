package com.ghosted.controller;

import com.ghosted.dto.*;
import com.ghosted.service.ApplicationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import com.ghosted.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> checkHealth() {
        return ResponseEntity.ok(ApiResponse.success("UP", "Backend is healthy and reachable at /api"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> createApplication(
            @Valid @RequestBody ApplicationRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApplicationResponseDTO response = applicationService.createApplication(userDetails.getId(), requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Application created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ApplicationResponseDTO>>> getApplications(
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<ApplicationResponseDTO> responses = applicationService.getAllApplicationsForUser(userDetails.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(responses, "Applications fetched successfully"));
    }

    // ─── Single Application ──────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> getApplicationById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApplicationResponseDTO response = applicationService.getApplicationById(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "Application fetched successfully"));
    }

    // ─── Status ──────────────────────────────────────────────────────────────────

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationStatusUpdateDTO statusUpdateDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApplicationResponseDTO response = applicationService.updateStatus(id, userDetails.getId(), statusUpdateDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Status updated successfully"));
    }

    // ─── Notes ───────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/notes")
    public ResponseEntity<ApiResponse<NoteResponseDTO>> addNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestDTO noteRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NoteResponseDTO response = applicationService.addNoteToApplication(id, userDetails.getId(), noteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Note added successfully"));
    }

    @GetMapping("/{id}/notes")
    public ResponseEntity<ApiResponse<List<NoteResponseDTO>>> getNotes(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<NoteResponseDTO> response = applicationService.getNotesForApplication(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "Notes fetched successfully"));
    }

    // ─── Global Interview Hub ─────────────────────────────────────────────────────

    @GetMapping("/interviews")
    public ResponseEntity<ApiResponse<java.util.List<InterviewResponseDTO>>> getAllInterviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        java.util.List<InterviewResponseDTO> response = applicationService.getAllInterviewsForUser(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "All interviews fetched successfully"));
    }

    // ─── Interview Management ─────────────────────────────────────────────────────

    @PostMapping("/{id}/interviews")
    public ResponseEntity<ApiResponse<InterviewResponseDTO>> addInterview(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody InterviewRequestDTO requestDTO) {
        InterviewResponseDTO response = applicationService.addInterview(id, userDetails.getId(), requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Interview round added successfully"));
    }

    @PutMapping("/{id}/interviews/{interviewId}")
    public ResponseEntity<ApiResponse<InterviewResponseDTO>> updateInterview(
            @PathVariable UUID id,
            @PathVariable UUID interviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody InterviewRequestDTO requestDTO) {
        InterviewResponseDTO response = applicationService.updateInterview(id, interviewId, userDetails.getId(), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Interview updated successfully"));
    }

    @DeleteMapping("/{id}/interviews/{interviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteInterview(
            @PathVariable UUID id,
            @PathVariable UUID interviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        applicationService.deleteInterview(id, interviewId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Interview deleted successfully"));
    }

    // ─── OA Management ───────────────────────────────────────────────────────────

    @PutMapping("/{id}/oa")
    public ResponseEntity<ApiResponse<OAResponseDTO>> updateOA(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody OARequestDTO requestDTO) {
        OAResponseDTO response = applicationService.updateOA(id, userDetails.getId(), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Online Assessment updated successfully"));
    }
}
