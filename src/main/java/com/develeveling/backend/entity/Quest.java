package com.develeveling.backend.entity;

import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "quests")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING) // This tells JPA to store the enum as a readable string
    @Column(nullable = false)
    private QuestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestCategory category;

    private int xpValue;

    private boolean completed = false;

    // Many Quests can belong to One User.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "quest_tags", joinColumns = @JoinColumn(name = "quest_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
}