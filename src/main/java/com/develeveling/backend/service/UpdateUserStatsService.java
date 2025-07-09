package com.develeveling.backend.service;

import com.develeveling.backend.entity.User;
import com.develeveling.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A scheduled service responsible for periodically updating user statistics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserStatsService {

    private final UserRepository userRepository;
    private final GitHubStatsProcessingService gitHubStatsProcessingService;

    /**
     * A nightly job that updates the GitHub contribution stats for all users.
     * Runs at 12:05 AM server time, just before the quest generation job.
     */
    @Scheduled(cron = "0 5 0 * * ?") // Runs at 12:05 AM daily
    @Transactional
    public void updateAllUserGitHubStats() {
        log.info("Starting nightly job: UpdateAllUserGitHubStats...");
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // This method modifies the user object in place
            gitHubStatsProcessingService.processGitHubActivityForUser(user);
        }

        // The @Transactional annotation will handle saving the modified user entities
        // when the method completes successfully.

        log.info("Finished nightly job: UpdateAllUserGitHubStats. Processed {} users.", users.size());
    }
}