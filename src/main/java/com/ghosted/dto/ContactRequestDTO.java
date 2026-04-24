package com.ghosted.dto;

import com.ghosted.entity.ContactCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ContactRequestDTO {

    @NotBlank
    private String name;

    private String email;

    private String phone;

    private String role;

    private ContactCategory category;

    private String linkedInUrl;

    private String notes;

    private String companyName;

    private String industry;

    private List<UUID> companyIds; // Optional link to companies
}
