package com.develeveling.backend.service.generator;

import com.develeveling.backend.dto.GitHubRepoDto;
import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import com.develeveling.backend.service.GitHubApiService;
import com.develeveling.backend.service.GitHubTokenService;
import com.develeveling.backend.service.generator.QuestGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GitHubNewProjectQuestGenerator implements QuestGenerator {

    private final GitHubApiService gitHubApiService;
    private final GitHubTokenService gitHubTokenService;

    @Override
    public Optional<Quest> generate(User user) {
        if (user.getGithubUsername() == null || user.getGithubUsername().isBlank()) {
            return Optional.empty();
        }

        try {
            String token = gitHubTokenService.getAccessTokenForUser(user.getId());
            List<GitHubRepoDto> repos = gitHubApiService.getRepositories(user.getGithubUsername(), token)
                    .collectList()
                    .block();

            if (repos != null && !repos.isEmpty()) {
                ZonedDateTime lastProjectDate = repos.get(0).createdAt();
                if (lastProjectDate.isAfter(ZonedDateTime.now().minusDays(30))) {
                    return Optional.empty();
                }
            }

            Quest quest = new Quest();
            quest.setUser(user);
            quest.setTitle("Launch a New Project");
            quest.setDescription("A great portfolio needs diverse projects. I'll create a new public repository on GitHub this week.");
            quest.setCategory(QuestCategory.PROGRAMMING);
            quest.setType(QuestType.WEEKLY);
            quest.setXpValue(100);
            quest.getTags().add("NEW_PROJECT");

            return Optional.of(quest);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}