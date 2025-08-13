package com.develeveling.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "leetcode_solve_tags", joinColumns = @JoinColumn(name = "solve_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
}
