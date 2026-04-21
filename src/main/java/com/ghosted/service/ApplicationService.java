package com.ghosted.service;

import com.ghosted.dto.ApplicationRequestDTO;
import com.ghosted.dto.ApplicationResponseDTO;
import com.ghosted.dto.ApplicationStatusUpdateDTO;
import com.ghosted.dto.NoteRequestDTO;
import com.ghosted.dto.NoteResponseDTO;
import com.ghosted.dto.InterviewRequestDTO;
import com.ghosted.dto.InterviewResponseDTO;
import com.ghosted.dto.OARequestDTO;
import com.ghosted.dto.OAResponseDTO;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationService {
    ApplicationResponseDTO createApplication(UUID userId, ApplicationRequestDTO requestDTO);
    ApplicationResponseDTO getApplicationById(UUID id, UUID userId);
    ApplicationResponseDTO updateStatus(UUID id, UUID userId, ApplicationStatusUpdateDTO statusUpdateDTO);
    Page<ApplicationResponseDTO> getAllApplicationsForUser(UUID userId, Pageable pageable);
    NoteResponseDTO addNoteToApplication(UUID applicationId, UUID userId, NoteRequestDTO noteRequestDTO);
    List<NoteResponseDTO> getNotesForApplication(UUID applicationId, UUID userId);

    // Interview Management
    InterviewResponseDTO addInterview(UUID applicationId, UUID userId, InterviewRequestDTO requestDTO);
    InterviewResponseDTO updateInterview(UUID applicationId, UUID interviewId, UUID userId, InterviewRequestDTO requestDTO);
    void deleteInterview(UUID applicationId, UUID interviewId, UUID userId);
    java.util.List<InterviewResponseDTO> getAllInterviewsForUser(UUID userId);

    // OA Management
    OAResponseDTO updateOA(UUID applicationId, UUID userId, OARequestDTO requestDTO);
}

