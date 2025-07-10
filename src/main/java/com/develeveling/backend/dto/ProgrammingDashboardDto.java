package com.develeveling.backend.dto;

import java.time.LocalDate;
import java.util.Map;

public record ProgrammingDashboardDto(
        // Key Metrics
        Integer githubCommitStreak,
        Integer githubCommitsLastWeek,
        Integer githubCommitsLastMonth,
        Integer githubCommitsLastYear,
        Long projectsStartedThisYear,

        // This is for the UI chart.
        Map<LocalDate, Integer> recentCommitHistory
) {}