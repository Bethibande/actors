package com.bethibande.actors.sharding.shard

import com.bethibande.actors.sharding.proto.Info.ShardInfo
import com.bethibande.actors.sharding.proto.Resources.Resource
import com.bethibande.actors.sharding.proto.shardInfo
import java.net.SocketAddress

data class Shard(
    val id: String,
    val address: SocketAddress,
    var state: ShardState,
    var resources: List<Resource>,
) {

    fun info(): ShardInfo = shardInfo {
        id = this@Shard.id
        address = this@Shard.address.toString()
        resources.addAll(this@Shard.resources)
    }

}
