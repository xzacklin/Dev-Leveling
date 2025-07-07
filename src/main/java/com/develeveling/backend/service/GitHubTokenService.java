package com.develeveling.backend.service;

import com.develeveling.backend.entity.User;
import com.develeveling.backend.entity.UserGitHubToken;
import com.develeveling.backend.repository.UserGitHubTokenRepository;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for managing GitHub OAuth2 access tokens.
 * This includes saving the token after a successful login and retrieving it for background jobs.
 */
@Service
public class GitHubTokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserGitHubTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public GitHubTokenService(OAuth2AuthorizedClientService authorizedClientService,
                              UserGitHubTokenRepository tokenRepository,
                              UserRepository userRepository) {
        this.authorizedClientService = authorizedClientService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the OAuth2 access token for the currently authenticated user from Spring Security's context,
     * finds the corresponding User entity, and saves the encrypted token to the database.
     *
     * @param authentication The Authentication object provided by Spring Security after a successful login.
     */
    @Transactional
    public void saveUserToken(Authentication authentication) {
        // Use the authorizedClientService to load the client details using the registrationId ("github")
        // and the principal's name (which is their GitHub username).
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "github",
                authentication.getName()
        );

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new IllegalStateException("Could not find authorized client or access token for GitHub.");
        }

        String githubUsername = authentication.getName();
        User user = userRepository.findByUsername(githubUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found with GitHub username: " + githubUsername));

        String rawAccessToken = authorizedClient.getAccessToken().getTokenValue();

        // Create or update the token entity and save it.
        // The AesEncryptor will automatically encrypt the rawAccessToken.
        UserGitHubToken userGitHubToken = new UserGitHubToken(user, rawAccessToken);
        tokenRepository.save(userGitHubToken);
    }

    /**
     * Retrieves the decrypted access token for a given user ID.
     * This is intended for use by background services like the nightly ETL job.
     *
     * @param userId The ID of the user whose token is needed.
     * @return The raw, decrypted access token.
     */
    public String getAccessTokenForUser(Long userId) {
        UserGitHubToken token = tokenRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("GitHub token not found for user ID: " + userId));

        // The AesEncryptor automatically decrypts the token when the entity is loaded.
        return token.getAccessToken();
    }
}