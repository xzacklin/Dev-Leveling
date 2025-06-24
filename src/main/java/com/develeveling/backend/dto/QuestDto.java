package com.develeveling.backend.dto;

// A record automatically creates private final fields, getters, equals(), hashCode(), and toString().
public record QuestDto(
        String title,
        String description,
        String category, //  ex: "INTERVIEW_PREP"
        String type,     // ex: "DAILY"
        int xpValue
) {
}