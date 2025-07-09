package com.develeveling.backend.dto;


public record LeaderboardEntryDto(
        String username,
        Long totalXp
) {
}