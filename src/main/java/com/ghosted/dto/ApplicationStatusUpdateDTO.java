package com.ghosted.dto;

import com.ghosted.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusUpdateDTO {

    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;
    
}
