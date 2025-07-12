package com.develeveling.backend.dto;

import com.develeveling.backend.model.NetworkingEventType;
import java.time.LocalDate;

public record NetworkingEventDto(
        Long id,
        NetworkingEventType eventType,
        String contactName,
        String company,
        LocalDate eventDate,
        String notes
) {}