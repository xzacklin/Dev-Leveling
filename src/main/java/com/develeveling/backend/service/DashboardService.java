package com.develeveling.backend.service;

import com.develeveling.backend.dto.ContributionCalendarDto;
import com.develeveling.backend.dto.GitHubRepoDto;
import com.develeveling.backend.dto.ProgrammingDashboardDto;
import com.develeveling.backend.dto.UserDashboardStatsDto;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.repository.QuestRepository;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final GitHubApiService gitHubApiService;
    private final GitHubTokenService gitHubTokenService;

    public UserDashboardStatsDto getDashboardStatsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("I can't find a user with ID: " + userId));

        ZonedDateTime oneWeekAgo = ZonedDateTime.now().minusWeeks(1);
        long questsThisWeek = questRepository.countCompletedQuestsAfter(user, oneWeekAgo);

        return new UserDashboardStatsDto(
                user.getGithubCommitStreak(),
                user.getGithubCommitsLastWeek(),
                questsThisWeek,
                user.getTotalXp(),
                user.getProgrammingXp(),
                user.getLeetcodeXp(),
                user.getInitiativeXp(),
                user.getNetworkingXp()
        );
    }

    public ProgrammingDashboardDto getProgrammingDashboardStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("I can't find a user with ID: " + userId));

        int totalContributions = 0;
        long projectsThisYear = 0;
        Map<LocalDate, Integer> commitHistory = Collections.emptyMap();

        if (user.getGithubUsername() != null && !user.getGithubUsername().isBlank()) {
            try {
                String token = gitHubTokenService.getAccessTokenForUser(user.getId());

                ContributionCalendarDto calendar = gitHubApiService.getContributionCalendar(user.getGithubUsername(), token).block();
                List<GitHubRepoDto> repos = gitHubApiService.getRepositories(user.getGithubUsername(), token).collectList().block();

                if (calendar != null) {
                    commitHistory = mapContributionsToDate(calendar);
                    totalContributions = calendar.data().user().contributionsCollection().contributionCalendar().totalContributions();
                }

                if (repos != null) {
                    int currentYear = ZonedDateTime.now().getYear();
                    projectsThisYear = repos.stream()
                            .filter(repo -> repo.createdAt().getYear() == currentYear)
                            .count();
                }

            } catch (Exception e) {
                log.error("Failed to fetch full GitHub data for user dashboard. User: {}", user.getUsername(), e);
            }
        }

        return new ProgrammingDashboardDto(
                user.getGithubCommitStreak(),
                user.getGithubCommitsLastWeek(),
                calculateCommitsLastMonth(commitHistory),
                totalContributions,
                projectsThisYear,
                commitHistory
        );
    }

    private Map<LocalDate, Integer> mapContributionsToDate(ContributionCalendarDto calendar) {
        return calendar.data().user().contributionsCollection().contributionCalendar().weeks().stream()
                .flatMap(week -> week.contributionDays().stream())
                .collect(Collectors.toMap(
                        day -> LocalDate.parse(day.date()),
                        ContributionCalendarDto.ContributionDay::contributionCount
                ));
    }

    private int calculateCommitsLastMonth(Map<LocalDate, Integer> contributions) {
        LocalDate today = LocalDate.now();
        return contributions.entrySet().stream()
                .filter(entry -> !entry.getKey().isAfter(today) && !entry.getKey().isBefore(today.minusDays(29)))
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
}