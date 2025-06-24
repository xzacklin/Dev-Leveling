package com.develeveling.backend.config; // Make sure this package name is correct for your project

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Step 1: Disable CSRF protection.
                .csrf(AbstractHttpConfigurer::disable)

                // Step 2: Set up authorization rules.
                .authorizeHttpRequests(authorize -> authorize
                        // These specific endpoints are allowed without authentication.
                        .requestMatchers("/api/v1/test/hello").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/quests/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}