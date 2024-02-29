package com.bethibande.actors.system

import com.bethibande.actors.Actor
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorType
import kotlin.jvm.Throws
import kotlin.reflect.KClass

/**
 * Represents an [ActorSystem] that manages the creation, existence, and closing of [Actor]s.
 */
interface ActorSystem<T: Actor<M>, M> {

    companion object {
        @JvmStatic
        fun <T: Actor<M>, M> localSystem(): ActorSystem<T, M> = LocalActorSystem<T, M>()
    }

    fun id(): String
    fun type(): KClass<T>
    fun actorType(): ActorType
    fun isClosed(): Boolean

    /**
     * Creates a new unique [Actor] instance.
     * @see newWithId
     */
    suspend fun new(): T

    /**
     * Creates a new [Actor] with the given [ActorId].
     * @throws IllegalArgumentException if an [Actor] with the given [ActorId] already exists.
     * @see new
     */
    @Throws(IllegalStateException::class)
    suspend fun newWithId(id: ActorId): T

    /**
     * Checks whether an [Actor] with the given [ActorId] exists.
     */
    suspend fun exists(id: ActorId): Boolean

    /**
     * Closes the given [Actor].
     * @param actor the [Actor] to close.
     */
    suspend fun close(actor: T)

    /**
     * Closes the [ActorSystem], closing all [Actor]s within the [ActorSystem].
     * @throws IllegalStateException if the [ActorSystem] is already closed.
     * @see isClosed
     */
    @Throws(IllegalStateException::class)
    suspend fun close()

    suspend fun all(): List<T>

}