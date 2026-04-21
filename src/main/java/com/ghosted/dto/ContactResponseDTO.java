package com.ghosted.dto;

import com.ghosted.entity.ContactCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ContactResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private ContactCategory category;
    private String notes;
    private List<CompanyDTO> companies;

    @Getter
    @Setter
    public static class CompanyDTO {
        private UUID id;
        private String name;
    }
}
