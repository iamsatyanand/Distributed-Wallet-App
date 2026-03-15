package com.satyanand.distributedwallet.service;

import com.satyanand.distributedwallet.api.dto.RegisterRequest;
import com.satyanand.distributedwallet.domain.enums.WalletStatus;
import com.satyanand.distributedwallet.domain.user.User;
import com.satyanand.distributedwallet.domain.user.UserRepository;
import com.satyanand.distributedwallet.domain.wallet.Wallet;
import com.satyanand.distributedwallet.domain.wallet.WalletRepository;
import com.satyanand.distributedwallet.shard.ShardContext;
import com.satyanand.distributedwallet.shard.ShardRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ShardRoutingService routingService;
    private final PasswordEncoder passwordEncoder;

    @Transactional // This applies to the DEFAULT (Global) DataSource
    public void registerUser(RegisterRequest request) {
        // 1. Check if user exists
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already taken");
        }

        // 2. Create User and calculate Shard Index [cite: 18, 77, 78]
        String userId = UUID.randomUUID().toString();
        int shardIndex = routingService.getShardIndex(userId);

        User user = new User();
        user.setId(userId);
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setShardIndex(shardIndex);
        userRepository.save(user);

        // 3. Create Wallet on the assigned Shard [cite: 20, 58]
        createInitialWallet(userId, shardIndex);
    }

    private void createInitialWallet(String userId, int shardIndex) {
        try {
            // Manually switch the "traffic controller" to the correct shard
            ShardContext.set(shardIndex); // cite: 58

            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(BigDecimal.ZERO); // Use DECIMAL(19,4) logic
            wallet.setCurrency("INR"); // cite: 27
            wallet.setStatus(WalletStatus.ACTIVE); // cite: 27
            walletRepository.save(wallet);
        } finally {
            ShardContext.clear(); // Essential to prevent cross-shard leakage [cite: 62]
        }
    }
}
