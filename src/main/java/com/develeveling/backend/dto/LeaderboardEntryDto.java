package com.develeveling.backend.dto; // Or your package name

/**
 * Represents a single user's entry on the leaderboard.
 * @param username The user's public username.
 * @param totalXp The calculated sum of all XP for this user.
 */
public record LeaderboardEntryDto(
        String username,
        Long totalXp
) {
}