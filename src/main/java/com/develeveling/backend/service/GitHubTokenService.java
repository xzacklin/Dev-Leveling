package com.develeveling.backend.service;

import com.develeveling.backend.entity.User;
import com.develeveling.backend.entity.UserGitHubToken;
import com.develeveling.backend.repository.UserGitHubTokenRepository;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GitHubTokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserGitHubTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveUserToken(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            return;
        }

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new IllegalStateException("I could not find the authorized client or access token for GitHub.");
        }

        String githubUsername = oauthToken.getPrincipal().getAttribute("login");
        if (githubUsername == null) {
            throw new IllegalStateException("I could not extract the GitHub username ('login' attribute) from the token.");
        }

        User user = userRepository.findByUsername(githubUsername)
                .orElseThrow(() -> new EntityNotFoundException("I could not find a user with the GitHub username: " + githubUsername));

        String rawAccessToken = authorizedClient.getAccessToken().getTokenValue();

        UserGitHubToken userGitHubToken = new UserGitHubToken(user, rawAccessToken);
        tokenRepository.save(userGitHubToken);
    }

    public String getAccessTokenForUser(Long userId) {
        UserGitHubToken token = tokenRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("GitHub token not found for user ID: " + userId));
        return token.getAccessToken();
    }
}