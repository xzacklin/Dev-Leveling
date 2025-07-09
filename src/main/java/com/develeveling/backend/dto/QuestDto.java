package com.develeveling.backend.dto;

public record QuestDto(
        String title,
        String description,
        String category, //  ex: "INTERVIEW_PREP"
        String type,     // ex: "DAILY"
        int xpValue
) {
}