package com.satyanand.distributedwallet.domain.user;

import com.satyanand.distributedwallet.common.BaseEntity;
import com.satyanand.distributedwallet.domain.enums.KycTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * User entity representing a user in the system.
 * Contains authentication details and KYC tier information.
 * The User table is Global (not sharded) because we need to
 * look up a user to find out which shard their wallet lives on.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Integer shardIndex; // Precomputed for this user

    @Enumerated(EnumType.STRING)
    private KycTier kycTier = KycTier.TIER_0; // Starts at Tier 0
}
