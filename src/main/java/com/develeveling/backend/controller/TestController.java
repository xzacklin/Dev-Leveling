package com.develeveling.backend.controller;

import com.develeveling.backend.dto.ContributionCalendarDto;
import com.develeveling.backend.service.GitHubApiService;
import com.develeveling.backend.service.QuestGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final QuestGenerationService questGenerationService;
    private final GitHubApiService gitHubApiService;

    @Value("${GITHUB_PAT}")
    private String githubPat;

    @PostMapping("/generate-quests")
    public ResponseEntity<String> triggerDailyQuestGeneration() {
        questGenerationService.generateAllDailyQuests();
        return ResponseEntity.ok("Daily quest generation job triggered successfully!");
    }

    @GetMapping("/github-calendar")
    public Mono<ResponseEntity<ContributionCalendarDto>> testGitHubCalendar(@RequestParam String username) {
        return gitHubApiService.getContributionCalendar(username, githubPat)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}