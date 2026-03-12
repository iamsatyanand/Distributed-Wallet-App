package com.satyanand.distributedwallet.shard;

public class ShardContext {

    private static final ThreadLocal<Integer> CONTEXT = new ThreadLocal<>();

    public static void set(Integer shardIndex) {
        CONTEXT.set(shardIndex);
    }

    public static Integer get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
