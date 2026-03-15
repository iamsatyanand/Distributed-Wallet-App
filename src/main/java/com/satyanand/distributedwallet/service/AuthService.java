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

    @Transactional
    public void registerUser(RegisterRequest request) {
        // Check if user exists
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already taken");
        }

        // Create User and calculate Shard Index
        String userId = UUID.randomUUID().toString();
        int shardIndex = routingService.getShardIndex(userId);

        User user = new User();
        user.setId(userId);
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setShardIndex(shardIndex);
        userRepository.save(user);

        // Create Wallet on the assigned Shard
        createInitialWallet(userId, shardIndex);
    }

    private void createInitialWallet(String userId, int shardIndex) {
        try {

            ShardContext.set(shardIndex);

            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setCurrency("INR");
            wallet.setStatus(WalletStatus.ACTIVE);
            walletRepository.save(wallet);
        } finally {
            ShardContext.clear(); // Essential to prevent cross-shard leakage
        }
    }
}
