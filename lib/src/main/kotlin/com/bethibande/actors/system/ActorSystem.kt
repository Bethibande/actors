package com.bethibande.actors.system

import com.bethibande.actors.behavior.Behavior

interface ActorSystem<A, C, S> {

    suspend fun new(state: S, id: ActorId? = null, behavior: Behavior<C, S>? = null): A

    suspend fun close()
    suspend fun isOpen(): Boolean

}