package com.develeveling.backend.service;

import com.develeveling.backend.dto.ContributionCalendarDto;
import com.develeveling.backend.dto.ProgrammingDashboardDto;
import com.develeveling.backend.dto.UserDashboardStatsDto;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.repository.QuestRepository;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final GitHubApiService gitHubApiService;
    private final GitHubTokenService gitHubTokenService;

    public UserDashboardStatsDto getDashboardStatsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("I can't find a user with ID: " + userId));

        ZonedDateTime oneWeekAgo = ZonedDateTime.now().minusWeeks(1);
        long questsThisWeek = questRepository.countCompletedQuestsAfter
                (user, oneWeekAgo);

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

        Map<LocalDate, Integer> commitHistory = getCommitHistory(user);

        return new ProgrammingDashboardDto(
                user.getGithubCommitStreak(),
                user.getGithubCommitsLastWeek(),
                commitHistory
        );
    }

    private Map<LocalDate, Integer> getCommitHistory(User user) {
        if (user.getGithubUsername() == null) {
            return Collections.emptyMap();
        }
        try {
            String token = gitHubTokenService.getAccessTokenForUser(user.getId());
            ContributionCalendarDto calendar = gitHubApiService.getContributionCalendar(user.getGithubUsername(), token).block();

            if (calendar == null) return Collections.emptyMap();

            return calendar.data().user().contributionsCollection().contributionCalendar().weeks().stream()
                    .flatMap(week -> week.contributionDays().stream())
                    .collect(Collectors.toMap(
                            day -> LocalDate.parse(day.date()),
                            ContributionCalendarDto.ContributionDay::contributionCount
                    ));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}