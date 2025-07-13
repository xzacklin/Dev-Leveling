package com.develeveling.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;

    @Data
    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String username;

        @Column(nullable = false)
        private String password;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(unique = true, nullable = true)
        private String githubUsername;

        // Scores for the four main categories
        @Column(nullable = true)
        private int programmingXp = 0;

        @Column(nullable = true)
        private int leetcodeXp = 0;

        @Column(nullable = true)
        private int initiativeXp = 0;

        @Column(nullable = true)
        private int networkingXp = 0;

        private long totalXp = 0;

        @Column(nullable = true)
        private int githubCommitStreak = 0;

        @Column(nullable = true)
        private int githubCommitsLastWeek = 0;

        private LocalDate githubLastCommitDate;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference("user-targetcompany")
        private List<TargetCompany> targetCompanies = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Quest> quests = new ArrayList<>();

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "user_target_roles", joinColumns = @JoinColumn(name = "user_id"))
        @Column(name = "role_name")
        private Set<String> targetRoles = new HashSet<>();

    }
