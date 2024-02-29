package com.bethibande.actors.system

import com.bethibande.actors.Actor
import com.bethibande.actors.context.ActorContext
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorType
import com.bethibande.actors.registry.LocalActorRegistry
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

class LocalActorSystem<T: Actor<M>, M>(
    private val id: String,
    private val type: KClass<T>,
    private val actorFactory: (ActorContext<T, M>) -> T,
): ActorSystem<T, M> {

    private val registry = LocalActorRegistry()
    private val actorType = ActorType.ofKClass(type)
    private val closed = AtomicBoolean(false)

    override fun id(): String = id

    override fun type(): KClass<T> = type

    override fun actorType(): ActorType = actorType

    override fun isClosed(): Boolean = closed.get()

    override suspend fun new(): T {
        TODO("Not yet implemented")
    }

    override suspend fun newWithId(id: ActorId): T {
        TODO("Not yet implemented")
    }

    override suspend fun exists(id: ActorId): Boolean = registry.has(id)

    override suspend fun close(actor: T) {
        if (!actor.isClosed()) actor.close()
        registry.unregister(actor)
    }

    override suspend fun close() {
        if (!closed.compareAndSet(false, true)) throw IllegalStateException("System is already closed.")
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun all(): List<T> = registry.all() as List<T>
}