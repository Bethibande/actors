package com.bethibande.actors.sharding.shard

sealed interface ShardState {

    data object Unknown: ShardState
    data object Available: ShardState
    data object Unavailable: ShardState

}