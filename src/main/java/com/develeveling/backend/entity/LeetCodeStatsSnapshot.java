package com.develeveling.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data @Entity @NoArgsConstructor
@Table(name = "leetcode_stats")
public class LeetCodeStatsSnapshot {

    @Id private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false) @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private int totalSolved, easySolved, mediumSolved, hardSolved;
    private int currentStreak, longestStreak, solvedPast30;

    @Lob @Column(columnDefinition = "TEXT")
    private String topicDifficultyJson;   // JSONB string

    @Lob @Column(columnDefinition = "TEXT")
    private String calendarJson;          // date→count JSON

    private Instant lastSyncedAt;
    private Boolean authValid = true;
}
