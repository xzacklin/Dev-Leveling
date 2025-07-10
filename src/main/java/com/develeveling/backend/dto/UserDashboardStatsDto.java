package com.develeveling.backend.dto;

public record UserDashboardStatsDto(
        // GitHub Stats
        Integer githubCommitStreak,
        Integer githubCommitsLastWeek,

        // Quest Stats
        long completedQuestsThisWeek,

        // Overall Progress
        long totalXp,
        int programmingXp,
        int leetcodeXp,
        int initiativeXp,
        int networkingXp
) {}