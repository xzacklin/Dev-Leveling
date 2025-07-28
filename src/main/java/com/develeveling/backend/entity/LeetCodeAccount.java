package com.develeveling.backend.entity;

import com.develeveling.backend.config.security.AesEncryptor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data @Entity @NoArgsConstructor
@Table(name = "leetcode_accounts")
public class LeetCodeAccount {

    @Id private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false) @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String username;

    @Convert(converter = AesEncryptor.class)
    @Column(nullable = false, length = 1024)
    private String sessionCookie;       // encrypted

    private Instant lastSyncedAt;
    private Boolean authValid = true;   // false when cookie expired
}
