package com.ghosted.service;

import com.ghosted.dto.ContactRequestDTO;
import com.ghosted.dto.ContactResponseDTO;
import com.ghosted.entity.Company;
import com.ghosted.entity.Contact;
import com.ghosted.entity.User;
import com.ghosted.exception.ResourceNotFoundException;
import com.ghosted.repository.CompanyRepository;
import com.ghosted.repository.ContactRepository;
import com.ghosted.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public ContactServiceImpl(ContactRepository contactRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public ContactResponseDTO createContact(UUID userId, ContactRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Contact contact = new Contact();
        contact.setUser(user);
        
        updateContactFields(contact, requestDTO);
        
        contact = contactRepository.save(contact);
        return mapToResponseDTO(contact);
    }

    @Override
    @Transactional
    public ContactResponseDTO updateContact(UUID id, UUID userId, ContactRequestDTO requestDTO) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        if (!contact.getUser().getId().equals(userId)) {
            throw new SecurityException("Not authorized to update this contact");
        }

        updateContactFields(contact, requestDTO);
        
        contact = contactRepository.save(contact);
        return mapToResponseDTO(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDTO> getAllContactsForUser(UUID userId, Pageable pageable) {
        return contactRepository.findByUserId(userId, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public void deleteContact(UUID id, UUID userId) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        if (!contact.getUser().getId().equals(userId)) {
            throw new SecurityException("Not authorized to delete this contact");
        }

        contactRepository.delete(contact);
    }

    private void updateContactFields(Contact contact, ContactRequestDTO requestDTO) {
        contact.setName(requestDTO.getName());
        contact.setEmail(requestDTO.getEmail());
        contact.setPhoneNumber(requestDTO.getPhoneNumber());
        contact.setRole(requestDTO.getRole());
        contact.setCategory(requestDTO.getCategory());
        contact.setNotes(requestDTO.getNotes());

        if (requestDTO.getCompanyIds() != null && !requestDTO.getCompanyIds().isEmpty()) {
            List<Company> companies = companyRepository.findAllById(requestDTO.getCompanyIds());
            contact.setCompanies(new HashSet<>(companies));
        } else {
            contact.setCompanies(new HashSet<>());
        }
    }

    private ContactResponseDTO mapToResponseDTO(Contact contact) {
        ContactResponseDTO dto = new ContactResponseDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhoneNumber(contact.getPhoneNumber());
        dto.setRole(contact.getRole());
        dto.setCategory(contact.getCategory());
        dto.setNotes(contact.getNotes());
        
        if (contact.getCompanies() != null) {
            List<ContactResponseDTO.CompanyDTO> companyDTOs = contact.getCompanies().stream().map(c -> {
                ContactResponseDTO.CompanyDTO cdto = new ContactResponseDTO.CompanyDTO();
                cdto.setId(c.getId());
                cdto.setName(c.getName());
                return cdto;
            }).collect(Collectors.toList());
            dto.setCompanies(companyDTOs);
        }
        return dto;
    }
}
