package com.satyanand.distributedwallet.domain.wallet;

import com.satyanand.distributedwallet.common.BaseEntity;
import com.satyanand.distributedwallet.domain.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter @Setter
public class Wallet extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO; // Mandatory precision

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal frozenBalance = BigDecimal.ZERO;

    @Column(length = 3, nullable = false)
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status = WalletStatus.ACTIVE;
}
