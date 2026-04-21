package com.ghosted.service;

import com.ghosted.dto.*;
import com.ghosted.entity.*;
import com.ghosted.exception.ResourceNotFoundException;
import com.ghosted.repository.*;
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
    private final InterviewRepository interviewRepository;
    private final OnlineAssessmentRepository oaRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                   UserRepository userRepository,
                                   CompanyRepository companyRepository,
                                   ContactRepository contactRepository,
                                   NoteRepository noteRepository,
                                   InterviewRepository interviewRepository,
                                   OnlineAssessmentRepository oaRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.noteRepository = noteRepository;
        this.interviewRepository = interviewRepository;
        this.oaRepository = oaRepository;
    }

    @Override
    @Transactional
    public ApplicationResponseDTO createApplication(UUID userId, ApplicationRequestDTO requestDTO) {
        try {
            log.info("Attempting to create application for user: {}, company: {}", userId, requestDTO.getCompanyName());
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Company company = companyRepository.findByNameIgnoreCase(requestDTO.getCompanyName().trim())
                    .orElseGet(() -> {
                        log.info("Creating new company: {}", requestDTO.getCompanyName());
                        Company newCompany = new Company();
                        newCompany.setName(requestDTO.getCompanyName().trim());
                        return companyRepository.save(newCompany);
                    });

            Contact contact = null;
            if (requestDTO.getContactId() != null) {
                log.info("Linking existing contact: {}", requestDTO.getContactId());
                contact = contactRepository.findById(requestDTO.getContactId())
                        .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
                
                if (!contact.getCompanies().contains(company)) {
                    contact.getCompanies().add(company);
                    contactRepository.save(contact);
                }
            } else if (requestDTO.getContactName() != null && !requestDTO.getContactName().trim().isEmpty()) {
                log.info("Creating new inline contact: {}", requestDTO.getContactName());
                Contact newContact = new Contact();
                newContact.setName(requestDTO.getContactName().trim());
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
            application.setJobTitle(requestDTO.getJobTitle().trim());
            application.setJobUrl(requestDTO.getJobUrl());
            application.setStatus(ApplicationStatus.APPLIED);
            application.setAppliedDate(java.time.LocalDate.now()); // Explicitly set

            log.info("Saving application entity...");
            application = applicationRepository.save(application);
            log.info("Application saved successfully with ID: {}", application.getId());

            return mapToResponseDTO(application);
        } catch (Exception e) {
            log.error("CRITICAL ERROR during application creation: {}", e.getMessage(), e);
            throw e; // Rethrow to let GlobalExceptionHandler handle it
        }
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

    @Override
    @Transactional
    public InterviewResponseDTO addInterview(UUID applicationId, UUID userId, InterviewRequestDTO requestDTO) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found for this user");
        }

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setType(requestDTO.getType());
        interview.setScheduledAt(requestDTO.getScheduledAt());
        interview.setMeetingLink(requestDTO.getMeetingLink());
        interview.setNotes(requestDTO.getNotes());
        interview.setStatus(InterviewStatus.SCHEDULED);

        interview = interviewRepository.save(interview);
        return mapToInterviewResponseDTO(interview);
    }

    @Override
    @Transactional
    public InterviewResponseDTO updateInterview(UUID applicationId, UUID interviewId, UUID userId, InterviewRequestDTO requestDTO) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview round not found"));

        if (!interview.getApplication().getId().equals(applicationId) || 
            !interview.getApplication().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Interview round not found for this application");
        }

        interview.setType(requestDTO.getType());
        interview.setScheduledAt(requestDTO.getScheduledAt());
        interview.setMeetingLink(requestDTO.getMeetingLink());
        interview.setNotes(requestDTO.getNotes());

        interview = interviewRepository.save(interview);
        return mapToInterviewResponseDTO(interview);
    }

    @Override
    @Transactional
    public void deleteInterview(UUID applicationId, UUID interviewId, UUID userId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview round not found"));

        if (!interview.getApplication().getId().equals(applicationId) || 
            !interview.getApplication().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Interview round not found for this application");
        }

        interviewRepository.delete(interview);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<InterviewResponseDTO> getAllInterviewsForUser(UUID userId) {
        return interviewRepository.findByApplicationUserIdOrderByScheduledAtAsc(userId)
                .stream()
                .map(this::mapToInterviewResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OAResponseDTO updateOA(UUID applicationId, UUID userId, OARequestDTO requestDTO) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found for this user");
        }

        OnlineAssessment oa = oaRepository.findByApplicationId(applicationId)
                .orElse(new OnlineAssessment());

        oa.setApplication(application);
        oa.setPlatform(requestDTO.getPlatform());
        oa.setDeadline(requestDTO.getDeadline());
        oa.setNotes(requestDTO.getNotes());
        if (oa.getId() == null) {
            oa.setStatus(OAStatus.PENDING);
        }

        oa = oaRepository.save(oa);
        return mapToOAResponseDTO(oa);
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
            dto.setContactId(application.getContact().getId());
            dto.setContactName(application.getContact().getName());
            dto.setContactEmail(application.getContact().getEmail());
            dto.setContactCategory(application.getContact().getCategory() != null ? 
                application.getContact().getCategory().name() : null);
        }

        // Fetch and map interviews
        dto.setInterviews(interviewRepository.findByApplicationIdOrderByScheduledAtAsc(application.getId())
                .stream()
                .map(this::mapToInterviewResponseDTO)
                .collect(Collectors.toList()));

        // Fetch and map OA
        oaRepository.findByApplicationId(application.getId())
                .ifPresent(oa -> dto.setOa(mapToOAResponseDTO(oa)));

        return dto;
    }

    private InterviewResponseDTO mapToInterviewResponseDTO(Interview interview) {
        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(interview.getId());
        dto.setType(interview.getType());
        dto.setStatus(interview.getStatus());
        dto.setScheduledAt(interview.getScheduledAt());
        dto.setMeetingLink(interview.getMeetingLink());
        dto.setNotes(interview.getNotes());
        return dto;
    }

    private OAResponseDTO mapToOAResponseDTO(OnlineAssessment oa) {
        OAResponseDTO dto = new OAResponseDTO();
        dto.setId(oa.getId());
        dto.setPlatform(oa.getPlatform());
        dto.setDeadline(oa.getDeadline());
        dto.setStatus(oa.getStatus());
        dto.setNotes(oa.getNotes());
        return dto;
    }
