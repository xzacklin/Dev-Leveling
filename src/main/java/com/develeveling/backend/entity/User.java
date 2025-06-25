package com.develeveling.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
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

        // Scores for the four main categories
        private int programmingXp = 0;
        private int leetcodeXp = 0;
        private int initiativeXp = 0;
        private int networkingXp = 0;

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "user_target_companies", joinColumns = @JoinColumn(name = "user_id"))
        @Column(name = "company_name")
        private Set<String> targetCompanies = new HashSet<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Quest> quests = new ArrayList<>();


    }
