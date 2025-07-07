package com.develeveling.backend.repository;

import com.develeveling.backend.entity.UserGitHubToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGitHubTokenRepository extends JpaRepository<UserGitHubToken, Long> {
    // Spring Data JPA will provide standard CRUD methods.
}