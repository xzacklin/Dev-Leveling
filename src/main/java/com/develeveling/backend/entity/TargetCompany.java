package com.develeveling.backend.entity;

import com.develeveling.backend.model.NetworkingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference; // I'm adding this import.
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "target_companies")
public class TargetCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-targetcompany")
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NetworkingStatus status = NetworkingStatus.IDENTIFIED;

    public TargetCompany(User user, String companyName) {
        this.user = user;
        this.companyName = companyName;
    }
}
