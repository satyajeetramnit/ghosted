package com.ghosted.controller;

import com.ghosted.dto.ApiResponse;
import com.ghosted.dto.ContactRequestDTO;
import com.ghosted.dto.ContactResponseDTO;
import com.ghosted.security.UserDetailsImpl;
import com.ghosted.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ContactResponseDTO>> createContact(
            @Valid @RequestBody ContactRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ContactResponseDTO response = contactService.createContact(userDetails.getId(), requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Contact created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ContactResponseDTO>>> getContacts(
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<ContactResponseDTO> responses = contactService.getAllContactsForUser(userDetails.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(responses, "Contacts fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactResponseDTO>> updateContact(
            @PathVariable UUID id,
            @Valid @RequestBody ContactRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ContactResponseDTO response = contactService.updateContact(id, userDetails.getId(), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Contact updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContact(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        contactService.deleteContact(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Contact deleted successfully"));
    }
}
