package com.develeveling.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Entity @NoArgsConstructor
@Table(name = "leetcode_solves",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","problemId"}))
public class LeetCodeProblemSolve {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer problemId;

    @Enumerated(EnumType.STRING) private Difficulty difficulty;

    private LocalDate firstSolvedAt;

    public enum Difficulty { EASY, MEDIUM, HARD }
}
