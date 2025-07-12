package com.develeveling.backend.entity;

import com.develeveling.backend.model.NetworkingEventType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "networking_events")
public class NetworkingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NetworkingEventType eventType;

    @Column(nullable = false)
    private String contactName;

    private String company;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(length = 1024)
    private String notes;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private ZonedDateTime createdAt;
}