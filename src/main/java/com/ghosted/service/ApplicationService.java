package com.ghosted.service;

import com.ghosted.dto.ApplicationRequestDTO;
import com.ghosted.dto.ApplicationResponseDTO;
import com.ghosted.dto.ApplicationStatusUpdateDTO;
import com.ghosted.dto.NoteRequestDTO;
import com.ghosted.dto.NoteResponseDTO;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationService {
    ApplicationResponseDTO createApplication(ApplicationRequestDTO requestDTO);
    ApplicationResponseDTO updateStatus(UUID id, ApplicationStatusUpdateDTO statusUpdateDTO);
    Page<ApplicationResponseDTO> getAllApplicationsForUser(UUID userId, Pageable pageable);
    NoteResponseDTO addNoteToApplication(UUID applicationId, NoteRequestDTO noteRequestDTO);
}
