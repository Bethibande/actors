package com.bethibande.actors.behavior

import com.bethibande.actors.AbstractActor
import java.util.Objects
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A behavior map representing a configurable list of behaviors.
 * Each configured behavior accepts a given type of command.
 * When the BehaviorMap accepts a command,
 * it routes the command to the behavior that is configured to handle the command.
 *
 * The BehaviorMap is backed by a HashMap.
 * It cannot route commands based on superclasses of the command.
 *
 * !! Important: this map is synchronized using a ReentrantReadWriteLock and could possibly lock actor coroutines when modified.
 *
 * @param C root command interface used by the actor
 * @param S state class used for the actor
 * @param A the type of the actor that will use the behavior
 */
class BehaviorMap<C, S>: Behavior<C, S> {

    private val lock = ReentrantReadWriteLock()
    private val behaviors = HashMap<Class<out C>, Behavior<out C, S>>()

    fun add(type: Class<out C>, behavior: Behavior<out C, S>) = lock.write {
        behaviors[type] = behavior
    }

    fun with(type: Class<out C>, behavior: Behavior<out C, S>) = apply { add(type, behavior) }

    @Suppress("UNCHECKED_CAST")
    fun get(type: Class<out C>): Behavior<C, S>? = lock.read {
        behaviors[type] as? Behavior<C, S>
    }

    override suspend fun accept(command: C, state: S, actor: AbstractActor<*, *>) {
        Objects.requireNonNull(command, "Command must not be null")
        val type = command!!::class.java as Class<out C>
        val behavior = get(type)
        behavior?.accept(command, state, actor)
    }
}