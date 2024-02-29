package com.bethibande.actors.registry

import com.bethibande.actors.Actor

interface ActorRegistry {

    suspend fun get(id: ActorId): Actor<*>?
    suspend fun register(actor: Actor<*>)
    suspend fun unregister(actor: Actor<*>)
    suspend fun has(id: ActorId): Boolean
    suspend fun all(): List<Actor<*>>

}