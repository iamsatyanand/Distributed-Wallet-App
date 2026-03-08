package com.satyanand.distributedwallet.shard;

import org.springframework.stereotype.Service;

@Service
public class ShardRoutingService {
    private static final int SHARD_COUNT = 4;

    public int getShardIndex(String userId) {

        return Math.abs(userId.hashCode()) % SHARD_COUNT;
    }
}
