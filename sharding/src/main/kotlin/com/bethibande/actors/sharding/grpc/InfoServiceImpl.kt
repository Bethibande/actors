package com.bethibande.actors.sharding.grpc

import com.bethibande.actors.sharding.ActorCluster
import com.bethibande.actors.sharding.proto.Info
import com.bethibande.actors.sharding.proto.InfoServiceGrpcKt
import com.google.protobuf.Empty

class InfoServiceImpl(private val cluster: ActorCluster): InfoServiceGrpcKt.InfoServiceCoroutineImplBase() {

    override suspend fun getShardInfo(request: Empty): Info.ShardInfo {
        return cluster.selfShard().info()
    }
}