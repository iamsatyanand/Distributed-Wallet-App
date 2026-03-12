package com.satyanand.distributedwallet.shard;

import org.springframework.stereotype.Service;

@Service
public class ShardRoutingService {
    private static final int SHARD_COUNT = 4;

    /***
     * implements userId-based sharding using hash mod (userId % N ).
     * This ensures that user's wallet data is consistently routed to the same shard,
     * enabling horizontal scaling and efficient data management.
     */
    public int getShardIndex(String userId) {

        return Math.abs(userId.hashCode()) % SHARD_COUNT;
    }
}
