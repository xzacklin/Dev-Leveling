package com.develeveling.backend.config.security;

import com.develeveling.backend.service.GitHubTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final GitHubTokenService gitHubTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 login successful for principal: {}. Saving token...", authentication.getName());
        gitHubTokenService.saveUserToken(authentication);

        // This redirect will fail since the frontend isn't running, which is expected.
        response.sendRedirect("http://localhost:3000/profile");
    }
}