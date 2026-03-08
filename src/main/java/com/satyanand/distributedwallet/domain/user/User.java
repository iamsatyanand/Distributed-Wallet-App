package com.satyanand.distributedwallet.domain.user;

import com.satyanand.distributedwallet.common.BaseEntity;
import com.satyanand.distributedwallet.domain.enums.KycTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username; // cite: 77

    @Column(unique = true, nullable = false)
    private String email; // cite: 77

    @Column(nullable = false)
    private String passwordHash; // cite: 77

    @Column(nullable = false)
    private Integer shardIndex; // Precomputed for this user [cite: 78]

    @Enumerated(EnumType.STRING)
    private KycTier kycTier = KycTier.TIER_0; // Starts at Tier 0 [cite: 188]
}
