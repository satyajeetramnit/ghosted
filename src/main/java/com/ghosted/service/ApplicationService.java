package com.ghosted.service;

import com.ghosted.dto.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationService {
    ApplicationResponseDTO createApplication(UUID userId, ApplicationRequestDTO requestDTO);
    ApplicationResponseDTO updateStatus(UUID id, ApplicationStatusUpdateDTO statusUpdateDTO);
    Page<ApplicationResponseDTO> getAllApplicationsForUser(UUID userId, Pageable pageable);
    NoteResponseDTO addNoteToApplication(UUID applicationId, NoteRequestDTO noteRequestDTO);

    // Interview Management
    InterviewResponseDTO addInterview(UUID applicationId, UUID userId, InterviewRequestDTO requestDTO);
    InterviewResponseDTO updateInterview(UUID applicationId, UUID interviewId, UUID userId, InterviewRequestDTO requestDTO);
    void deleteInterview(UUID applicationId, UUID interviewId, UUID userId);
    java.util.List<InterviewResponseDTO> getAllInterviewsForUser(UUID userId);

    // OA Management
    OAResponseDTO updateOA(UUID applicationId, UUID userId, OARequestDTO requestDTO);
}
