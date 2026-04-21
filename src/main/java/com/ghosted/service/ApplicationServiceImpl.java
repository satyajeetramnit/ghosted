package com.ghosted.service;

import com.ghosted.dto.ApplicationRequestDTO;
import com.ghosted.dto.ApplicationResponseDTO;
import com.ghosted.dto.ApplicationStatusUpdateDTO;
import com.ghosted.dto.NoteRequestDTO;
import com.ghosted.dto.NoteResponseDTO;
import com.ghosted.entity.Application;
import com.ghosted.entity.ApplicationStatus;
import com.ghosted.entity.Company;
import com.ghosted.entity.Contact;
import com.ghosted.entity.Note;
import com.ghosted.entity.User;
import com.ghosted.exception.ResourceNotFoundException;
import com.ghosted.repository.ApplicationRepository;
import com.ghosted.repository.CompanyRepository;
import com.ghosted.repository.ContactRepository;
import com.ghosted.repository.NoteRepository;
import com.ghosted.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final NoteRepository noteRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  UserRepository userRepository,
                                  CompanyRepository companyRepository,
                                  ContactRepository contactRepository,
                                  NoteRepository noteRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    @Transactional
    public ApplicationResponseDTO createApplication(UUID userId, ApplicationRequestDTO requestDTO) {
        log.info("Creating application for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Company company = companyRepository.findByNameIgnoreCase(requestDTO.getCompanyName())
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setName(requestDTO.getCompanyName());
                    return companyRepository.save(newCompany);
                });

        Contact contact = null;
        if (requestDTO.getContactName() != null && !requestDTO.getContactName().trim().isEmpty()) {
            Contact newContact = new Contact();
            newContact.setName(requestDTO.getContactName());
            newContact.setEmail(requestDTO.getContactEmail());
            newContact.setUser(user);
            newContact.getCompanies().add(company);
            newContact.setCategory(com.ghosted.entity.ContactCategory.HR);
            contact = contactRepository.save(newContact);
        }

        Application application = new Application();
        application.setUser(user);
        application.setCompany(company);
        application.setContact(contact);
        application.setJobTitle(requestDTO.getJobTitle());
        application.setJobUrl(requestDTO.getJobUrl());
        application.setStatus(ApplicationStatus.APPLIED);

        application = applicationRepository.save(application);

        return mapToResponseDTO(application);
    }

    @Override
    @Transactional
    public ApplicationResponseDTO updateStatus(UUID id, ApplicationStatusUpdateDTO statusUpdateDTO) {
        log.info("Updating status for application: {} to {}", id, statusUpdateDTO.getStatus());
        
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(statusUpdateDTO.getStatus());
        application = applicationRepository.save(application);

        return mapToResponseDTO(application);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationResponseDTO> getAllApplicationsForUser(UUID userId, Pageable pageable) {
        log.info("Fetching applications for user: {}", userId);
        return applicationRepository.findByUserId(userId, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public NoteResponseDTO addNoteToApplication(UUID applicationId, NoteRequestDTO noteRequestDTO) {
        log.info("Adding note to application: {}", applicationId);
        
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Note note = new Note();
        note.setApplication(application);
        note.setContent(noteRequestDTO.getContent());

        note = noteRepository.save(note);

        NoteResponseDTO responseDTO = new NoteResponseDTO();
        responseDTO.setId(note.getId());
        responseDTO.setContent(note.getContent());
        responseDTO.setCreatedAt(note.getCreatedAt());
        return responseDTO;
    }

    private ApplicationResponseDTO mapToResponseDTO(Application application) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(application.getId());
        dto.setCompanyName(application.getCompany().getName());
        dto.setJobTitle(application.getJobTitle());
        dto.setJobUrl(application.getJobUrl());
        dto.setStatus(application.getStatus());
        dto.setAppliedDate(application.getAppliedDate());
        dto.setFollowUpDate(application.getFollowUpDate());
        if (application.getContact() != null) {
            dto.setContactName(application.getContact().getName());
        }
        return dto;
    }
}
