package com.ghosted.service;

import com.ghosted.dto.ContactRequestDTO;
import com.ghosted.dto.ContactResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ContactService {
    ContactResponseDTO createContact(UUID userId, ContactRequestDTO requestDTO);
    ContactResponseDTO updateContact(UUID id, UUID userId, ContactRequestDTO requestDTO);
    Page<ContactResponseDTO> getAllContactsForUser(UUID userId, Pageable pageable);
    void deleteContact(UUID id, UUID userId);
}
