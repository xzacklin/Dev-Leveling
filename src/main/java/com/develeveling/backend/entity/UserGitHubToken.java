package com.develeveling.backend.entity;

import com.develeveling.backend.config.security.AesEncryptor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a securely stored GitHub OAuth2 access token for a user.
 * This entity has a one-to-one relationship with the main User entity.
 * Seperated from User Entity to remove unessecary constant encryption compute.
 */
@Entity
@Table(name = "user_github_tokens")
@Data
@NoArgsConstructor
public class UserGitHubToken {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // This annotation links the @Id of this entity to the User's ID.
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The encrypted OAuth2 access token-automatically encrypted with AES
     */
    @Convert(converter = AesEncryptor.class)
    @Column(nullable = false, length = 1024)
    private String accessToken;

    public UserGitHubToken(User user, String accessToken) {
        this.user = user;
        this.userId = user.getId();
        this.accessToken = accessToken;
    }
}