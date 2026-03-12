package com.satyanand.distributedwallet.shard;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ShardRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected @Nullable Object determineCurrentLookupKey() {
        // This looks at a "ThreadLocal" variable to see which shard index is set
        return ShardContext.get();
    }
}
