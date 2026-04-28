package com.ghosted.dto;

import com.ghosted.entity.ContactCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApplicationContactDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String linkedInUrl;
    private ContactCategory category;
}
