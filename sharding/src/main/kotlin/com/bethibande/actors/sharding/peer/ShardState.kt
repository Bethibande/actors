package com.bethibande.actors.sharding.peer

sealed interface ShardState {

    data object Unknown: ShardState
    data object Available: ShardState
    data object Unavailable: ShardState

}