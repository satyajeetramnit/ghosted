package com.ghosted.controller;

import com.ghosted.dto.ApiResponse;
import com.ghosted.dto.ApplicationRequestDTO;
import com.ghosted.dto.ApplicationResponseDTO;
import com.ghosted.dto.ApplicationStatusUpdateDTO;
import com.ghosted.dto.NoteRequestDTO;
import com.ghosted.dto.NoteResponseDTO;
import com.ghosted.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> createApplication(
            @Valid @RequestBody ApplicationRequestDTO requestDTO) {
        ApplicationResponseDTO response = applicationService.createApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Application created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ApplicationResponseDTO>>> getApplications(
            @RequestParam UUID userId, Pageable pageable) {
        Page<ApplicationResponseDTO> responses = applicationService.getAllApplicationsForUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(responses, "Applications fetched successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationStatusUpdateDTO statusUpdateDTO) {
        ApplicationResponseDTO response = applicationService.updateStatus(id, statusUpdateDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Status updated successfully"));
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<ApiResponse<NoteResponseDTO>> addNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestDTO noteRequestDTO) {
        NoteResponseDTO response = applicationService.addNoteToApplication(id, noteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Note added successfully"));
    }
}
