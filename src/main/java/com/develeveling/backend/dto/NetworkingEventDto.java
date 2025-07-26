package com.develeveling.backend.dto;

import com.develeveling.backend.model.NetworkingEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record NetworkingEventDto(
        Long id,
        @NotNull NetworkingEventType eventType,
        @NotBlank String contactName,
        @NotBlank String company,
        @PastOrPresent LocalDate eventDate,
        String notes
) {}
