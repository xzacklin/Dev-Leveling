package com.develeveling.backend.repository;

import com.develeveling.backend.dto.LeaderboardEntryDto;
import com.develeveling.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByGithubUsername(String githubUsername);

    @Query("SELECT new com.develeveling.backend.dto.LeaderboardEntryDto(u.username, u.totalXp) " +
            "FROM User u " +
            "ORDER BY u.totalXp DESC")
    List<LeaderboardEntryDto> findTopUsersByTotalXp(Pageable pageable);
}