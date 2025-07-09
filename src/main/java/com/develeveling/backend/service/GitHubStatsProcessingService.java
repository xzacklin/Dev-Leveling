package com.develeveling.backend.service;

import com.develeveling.backend.dto.ContributionCalendarDto;
import com.develeveling.backend.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Processes raw GitHub activity data to calculate meaningful, persistent stats for a user.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubStatsProcessingService {

    private final GitHubApiService gitHubApiService;
    private final GitHubTokenService gitHubTokenService;

    /**
     * Fetches a user's GitHub activity, processes it, and updates the User entity with calculated stats.
     */
    public void processGitHubActivityForUser(User user) {
        if (user.getGithubUsername() == null || user.getGithubUsername().isBlank()) {
            return; // Skip users without a linked GitHub account.
        }

        try {
            String accessToken = gitHubTokenService.getAccessTokenForUser(user.getId());
            ContributionCalendarDto calendar = gitHubApiService.getContributionCalendar(user.getGithubUsername(), accessToken).block();

            if (calendar == null) {
                log.warn("Contribution calendar for user {} was null. Skipping processing.", user.getUsername());
                return;
            }

            // Create a Map for efficient, date-based lookups of contribution counts.
            Map<LocalDate, Integer> contributionsByDate = mapContributionsToDate(calendar);

            // Calculate and set the new stats on the user object.
            user.setGithubCommitStreak(calculateCurrentStreak(contributionsByDate));
            user.setGithubCommitsLastWeek(calculateCommitsLastWeek(contributionsByDate));
            user.setGithubLastCommitDate(findLastCommitDate(contributionsByDate));

        } catch (Exception e) {
            log.error("Failed to process GitHub stats for user: {}. Reason: {}", user.getUsername(), e.getMessage());
        }
    }

    private Map<LocalDate, Integer> mapContributionsToDate(ContributionCalendarDto calendar) {
        return calendar.data().user().contributionsCollection().contributionCalendar().weeks().stream()
                .flatMap(week -> week.contributionDays().stream())
                .collect(Collectors.toMap(
                        day -> LocalDate.parse(day.date()),
                        ContributionCalendarDto.ContributionDay::contributionCount
                ));
    }

    private int calculateCurrentStreak(Map<LocalDate, Integer> contributions) {
        int streak = 0;
        LocalDate cursor = LocalDate.now().minusDays(1);

        while (contributions.getOrDefault(cursor, 0) > 0) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private int calculateCommitsLastWeek(Map<LocalDate, Integer> contributions) {
        LocalDate today = LocalDate.now();
        return contributions.entrySet().stream()
                .filter(entry -> !entry.getKey().isAfter(today) && !entry.getKey().isBefore(today.minusDays(7)))
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private LocalDate findLastCommitDate(Map<LocalDate, Integer> contributions) {
        return contributions.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .max(LocalDate::compareTo)
                .orElse(null);
    }
}