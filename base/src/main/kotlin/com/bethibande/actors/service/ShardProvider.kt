package com.bethibande.actors.service

interface ShardProvider {

    /**
     * Add a listener that is invoked every time a shard is removed
     */
    fun onShardRemoved(fn: (ShardInstance) -> Unit)

    /**
     * Add a listener which is invoked once the given shard is removed from the cluster
     */
    fun onShardRemoved(shard: ShardInstance, fn: (ShardInstance) -> Unit)

    fun shards(): List<ShardInstance>

}