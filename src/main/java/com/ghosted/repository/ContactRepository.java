package com.ghosted.repository;

import com.ghosted.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    
    Page<Contact> findByUserId(UUID userId, Pageable pageable);
}

