package com.bethibande.actors.sharding.peer

import java.net.SocketAddress

data class Shard(
    val address: SocketAddress,
    var state: ShardState,
)
