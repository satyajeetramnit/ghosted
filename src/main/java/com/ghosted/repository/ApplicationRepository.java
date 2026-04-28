package com.ghosted.repository;

import com.ghosted.entity.Application;
import com.ghosted.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    
    @EntityGraph(attributePaths = {"company", "contacts"})
    Page<Application> findByUserId(UUID userId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"company", "contacts"})
    Page<Application> findByUserIdAndStatus(UUID userId, ApplicationStatus status, Pageable pageable);

    java.util.List<Application> findByContactsId(UUID contactId);
}
