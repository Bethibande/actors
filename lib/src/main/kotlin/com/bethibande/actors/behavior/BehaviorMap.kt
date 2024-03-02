package com.bethibande.actors.behavior

import java.util.Objects

/**
 * A behavior map representing a configurable list of behaviors.
 * Each configured behavior accepts a given type of command.
 * When the BehaviorMap accepts a command,
 * it routes the command to the behavior that is configured to handle the command.
 *
 * The BehaviorMap is backed by a HashMap.
 * It cannot route commands based on superclasses of the command.
 *
 * @param C root command interface used by the actor
 * @param S state class used for the actor
 */
class BehaviorMap<C, S>: Behavior<C, S> {

    private val behaviors = HashMap<Class<out C>, Behavior<out C, S>>()

    fun <T: C> add(commandType: Class<T>, behavior: Behavior<T, S>) {
        behaviors[commandType] = behavior
    }

    fun <T: C> with(commandType: Class<T>, behavior: Behavior<T, S>) = apply {
        add(commandType, behavior)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: C> get(commandType: Class<T>): Behavior<T, S>? {
        return behaviors[commandType] as? Behavior<T, S>
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun accept(command: C, state: S) {
        Objects.requireNonNull("command must not be null")
        val type = command!!::class.java as Class<C>
        val behavior = get(type)
        behavior?.accept(command, state)
    }
}