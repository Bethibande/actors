package com.bethibande.actors.registry

import com.bethibande.actors.Actor

class LocalActorRegistry: ActorRegistry {

    private val register = mutableMapOf<ActorId, Actor<*>>()

    override suspend fun get(id: ActorId): Actor<*>? = register[id]

    override suspend fun register(actor: Actor<*>) {
        register[actor.id()] = actor
    }

    override suspend fun unregister(actor: Actor<*>) {
        register.remove(actor.id())
    }

    override suspend fun has(id: ActorId): Boolean = register.containsKey(id)

    override suspend fun all(): List<Actor<*>> = register.values.toList()
}