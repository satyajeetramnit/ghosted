package com.ghosted.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateAppliedDateDTO {
    @NotNull(message = "Applied date is required")
    private LocalDate appliedDate;
}
