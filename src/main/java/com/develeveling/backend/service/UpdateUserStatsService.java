package com.develeveling.backend.service;

import com.develeveling.backend.dto.GitHubRepoDto;
import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.repository.QuestRepository;
import com.develeveling.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserStatsService {

    private final UserRepository userRepository;
    private final GitHubStatsProcessingService gitHubStatsProcessingService;
    private final QuestRepository questRepository;
    private final GitHubApiService gitHubApiService;
    private final GitHubTokenService gitHubTokenService;
    private final QuestService questService;

    @Scheduled(cron = "0 5 0 * * ?")
    @Transactional
    public void updateAllUserGitHubStats() {
        log.info("Starting nightly job: UpdateAllUserGitHubStats...");
        List<User> users = userRepository.findAll();

        for (User user : users) {
            gitHubStatsProcessingService.processGitHubActivityForUser(user);
            verifyNewProjectQuest(user);
        }

        log.info("Finished nightly job: UpdateAllUserGitHubStats. Processed {} users.", users.size());
    }

    private void verifyNewProjectQuest(User user) {
        if (user.getGithubUsername() == null) return;

        questRepository.findByUserAndCompletedIsFalse(user).stream()
                .filter(quest -> quest.getTags().contains("NEW_PROJECT"))
                .findFirst()
                .ifPresent(quest -> {
                    try {
                        String token = gitHubTokenService.getAccessTokenForUser(user.getId());
                        List<GitHubRepoDto> repos = gitHubApiService.getRepositories(user.getGithubUsername(), token)
                                .collectList()
                                .block();

                        if (repos == null) return;

                        boolean newRepoExists = repos.stream()
                                .anyMatch(repo -> repo.createdAt().isAfter(quest.getCreatedAt()));

                        if (newRepoExists) {
                            log.info("Completing NEW_PROJECT quest {} for user {}", quest.getId(), user.getUsername());
                            questService.completeQuest(quest.getId());
                        }
                    } catch (Exception e) {
                        log.error("Failed to verify NEW_PROJECT quest for user {}", user.getUsername(), e);
                    }
                });
    }
}