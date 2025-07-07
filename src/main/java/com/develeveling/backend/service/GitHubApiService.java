package com.develeveling.backend.service;

import com.develeveling.backend.dto.CommitActivityStatsDto;
import com.develeveling.backend.dto.ContributionCalendarDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class GitHubApiService {

    private static final Logger log = LoggerFactory.getLogger(GitHubApiService.class);
    private final WebClient webClient;

    @Autowired
    public GitHubApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    /**
     * Fetches the commit activity for the last year for a given repository.
     * Includes a retry mechanism for handling rate limits.
     */
    public Mono<List<CommitActivityStatsDto>> getCommitActivity(String owner, String repo, String accessToken) {
        return this.webClient.get()
                .uri("/repos/{owner}/{repo}/stats/commit_activity", owner, repo)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToFlux(CommitActivityStatsDto.class)
                .collectList()
                .retryWhen(getRetrySpec("commit activity for " + owner + "/" + repo))
                .onErrorResume(e -> {
                    log.error("Failed to fetch commit activity for {}/{} after retries. Error: {}", owner, repo, e.getMessage());
                    return Mono.empty(); // Return an empty Mono on final failure
                });
    }

    /**
     * Fetches the user's contribution calendar data using the GitHub GraphQL API.
     */
    public Mono<ContributionCalendarDto> getContributionCalendar(String username, String accessToken) {
        String graphqlQuery = "{ \"query\": \"query { user(login: \\\"" + username + "\\\") { contributionsCollection { contributionCalendar { totalContributions weeks { contributionDays { contributionCount date } } } } } }\" }";

        return this.webClient.post()
                .uri("/graphql")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .bodyValue(graphqlQuery)
                .retrieve()
                .bodyToMono(ContributionCalendarDto.class)
                .retryWhen(getRetrySpec("contribution calendar for " + username))
                .onErrorResume(e -> {
                    log.error("Failed to fetch contribution calendar for {} after retries. Error: {}", username, e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Creates a reusable Retry specification for WebClient calls.
     * It retries up to 3 times on specific HTTP status codes (like 403 for rate limits)
     * with an exponential backoff strategy.
     */
    private Retry getRetrySpec(String context) {
        return Retry.backoff(3, Duration.ofSeconds(2))
                .filter(this::isRateLimitError)
                .doBeforeRetry(retrySignal -> log.warn("Retrying... Attempt #{}. Error for {}: {}",
                        retrySignal.totalRetries() + 1, context, retrySignal.failure().getMessage()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    log.error("Retries exhausted for {}. Final error: {}", context, retrySignal.failure().getMessage());
                    return retrySignal.failure();
                });
    }

    /**
     * Checks if a given exception is a 403 Forbidden error, which often indicates a rate limit issue.
     */
    private boolean isRateLimitError(Throwable throwable) {
        return throwable instanceof WebClientResponseException.Forbidden;
    }
}
