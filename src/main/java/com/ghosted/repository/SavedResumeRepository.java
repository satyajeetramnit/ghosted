package com.ghosted.repository;

import com.ghosted.entity.SavedResume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavedResumeRepository extends JpaRepository<SavedResume, UUID> {
    List<SavedResume> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<SavedResume> findByIdAndUserId(UUID id, UUID userId);
}
