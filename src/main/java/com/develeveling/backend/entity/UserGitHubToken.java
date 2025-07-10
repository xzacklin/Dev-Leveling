package com.develeveling.backend.entity;

import com.develeveling.backend.config.security.AesEncryptor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_github_tokens")
@Data
@NoArgsConstructor
public class UserGitHubToken {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


    @Convert(converter = AesEncryptor.class)
    @Column(nullable = false, length = 1024)
    private String accessToken;

    public UserGitHubToken(User user, String accessToken) {
        this.user = user;
        this.userId = user.getId();
        this.accessToken = accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}