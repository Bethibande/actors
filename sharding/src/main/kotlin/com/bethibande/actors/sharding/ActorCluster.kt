package com.bethibande.actors.sharding

import com.bethibande.actors.sharding.peer.Shard
import com.bethibande.actors.sharding.peer.ShardState
import java.net.SocketAddress
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ActorCluster(private val bind: SocketAddress, private val initPeers: List<SocketAddress>) {

    private val mutex = Mutex()
    private val shards: MutableList<Shard> = addressesToPeers(initPeers)

    suspend fun start() {
        mutex.withLock {
            shards.forEach { shard -> validate(shard) }
            shards.removeAll { shard -> shard.state !is ShardState.Available }
        }
    }

    private fun addressesToPeers(addresses: List<SocketAddress>): MutableList<Shard> {
        return addresses.map { Shard(it, ShardState.Unknown) }.toMutableList()
    }

    /**
     * Check if the shard is still valid / reachable
     */
    private suspend fun validate(shard: Shard) {

    }

}