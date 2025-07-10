package com.develeveling.backend.dto;

import java.time.LocalDate;
import java.util.Map;

public record ProgrammingDashboardDto(
        // Key Metrics
        Integer githubCommitStreak,
        Integer githubCommitsLastWeek,

        Map<LocalDate, Integer> recentCommitHistory
) {}